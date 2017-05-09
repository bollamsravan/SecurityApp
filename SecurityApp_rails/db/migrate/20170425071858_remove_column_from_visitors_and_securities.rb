class RemoveColumnFromVisitorsAndSecurities < ActiveRecord::Migration[5.0]
  def change
    remove_column :visitors,:vis_id
    remove_column :securities, :sec_id
  end
end
