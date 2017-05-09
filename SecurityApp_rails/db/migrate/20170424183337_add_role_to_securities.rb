class AddRoleToSecurities < ActiveRecord::Migration[5.0]
  def change
    add_column :securities, :role, :int
  end
end
