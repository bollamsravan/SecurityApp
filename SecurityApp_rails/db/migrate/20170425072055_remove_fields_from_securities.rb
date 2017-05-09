class RemoveFieldsFromSecurities < ActiveRecord::Migration[5.0]
  def change
    remove_column :securities,  :name
    remove_column :securities, :phone
    remove_column :securities, :role
  end
end
