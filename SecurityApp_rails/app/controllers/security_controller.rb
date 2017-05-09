class SecurityController< ApplicationController
  skip_before_action :authenticate_request, only: :register
  before_action :check_security, only: :add_visitor
  before_action :get_security, only: :add_visitor

  def register
    @security = Security.new
    @user = User.create(user_params)
    @user.role = ROLE_SECURITY
    if @user.save
      @security.user_id = @user.id
      if @security.save
        render json:{security: @security.as_json(include: :user), status:STATUS_CREATED, auth_token:AuthenticateUser.call(user_params["email"],user_params["password"]).result}
      else
        render json:{status: STATUS_BAD_REQUEST, errors: @security.errors}
      end
    else
      render json:{status: STATUS_BAD_REQUEST,errors: @user.errors}
    end
  end

  def add_visitor
    name = params[:name]
    phone = params[:phone]
    address = params[:address]
    purpose = params[:purpose]
    door_no = params[:door_no]
    r = @security.add_visitor(name,phone,address,purpose,door_no)
    if r[0]
      render json: {status: STATUS_CREATED}
    else
      render json: {status: STATUS_BAD_REQUEST,errors: r[1]}
    end
  end

  private
  def get_security
    @security = Security.find_by_user_id(@current_user.id)
  end
  def check_security
    if @current_user.role !=ROLE_SECURITY
        render json: {errors: "Not a Security",status: STATUS_BAD_REQUEST }
    end
  end
  def user_params
    params.require(:user).permit(:email, :password, :password_confirmation,:name, :mobilenumber)
  end

end