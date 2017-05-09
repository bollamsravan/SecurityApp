class UserController < ApplicationController


  def availability
    user_id = params['user_id']
    @owner = User.find(user_id)
    render json:{availability: @owner.availability,status: STATUS_OK}
  end

  def set_availability
    @current_user.availability = params['availability']
    if @current_user.save
      render json: {status: STATUS_OK}
    else
      render json: {status: STATUS_BAD_REQUEST}
    end
  end
  private
  def user_params
    params.require(:user).permit(:email, :password, :password_confirmation,:name, :mobilenumber)
  end


end