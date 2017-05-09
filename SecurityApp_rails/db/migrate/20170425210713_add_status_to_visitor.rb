class AddStatusToVisitor < ActiveRecord::Migration[5.0]
  def change
    add_column :visitors, :status, :integer, default: 2001
  end
end
