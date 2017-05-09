class ResidentController < ApplicationController
	skip_before_action :authenticate_request, only: :register
	before_action :get_resident, only: [:get_visitor_notifications, :accept_or_reject_visitor,:set_availability,:resident_availability]

	def availability
		door_no = params['door_no']
		@resident = Resident.find_by_door_no(door_no)
		render json:{availability: @resident.availability,status: STATUS_OK}
	end

	def resident_availability
		render json: {availability: @resident.availability,status:  STATUS_OK}
	end
	def set_availability
		@resident.availability = params['availability']
		if @resident.save
			render json: {status: STATUS_OK}
		else
			render json: {status: STATUS_BAD_REQUEST,errors: @resident.errors}
		end
	end

	def register
		@resident = Resident.new(door_no: params['door_no'], residence: params['residence'])
		@user = User.create(user_params)
		@user.role = ROLE_RESIDENT
		if @user.save
			@resident.user_id = @user.id
			if @resident.save
				render json:{resident: @resident.as_json(include: :user),status: STATUS_CREATED,auth_token:AuthenticateUser.call(user_params["email"],user_params["password"]).result }
			else
				render json:{status: STATUS_BAD_REQUEST, errors: @resident.errors}
			end
		else
			render json:{status: STATUS_BAD_REQUEST,errors: @user.errors}
		end
	end

	def get_visitor_notifications
		@visitors  = @resident.get_notifications
		render json: {visitors: @visitors.as_json,status: STATUS_OK}
	end

	def accept_or_reject_visitor
		if @resident.accept_or_reject_visitor(params['visitor_id'],params['acceptance'])
			render json: {status: STATUS_OK}
		else
			render json: {status: STATUS_BAD_REQUEST}
		end
	end


	private
	def get_resident
		@resident = Resident.find_by_user_id(@current_user.id)
	end
	def user_params
		params.require(:user).permit(:email, :password, :password_confirmation,:name, :mobilenumber)
	end
end
