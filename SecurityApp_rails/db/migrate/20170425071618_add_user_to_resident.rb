class AddUserToResident < ActiveRecord::Migration[5.0]
  def change
    add_reference :residents, :user,index: true
    add_reference :securities, :user,index: true
  end
end
