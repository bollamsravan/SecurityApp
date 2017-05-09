class RemoveColoumnAvaiilabilityFromUsers < ActiveRecord::Migration[5.0]
  def change
    remove_column :users, :availability
  end
end
