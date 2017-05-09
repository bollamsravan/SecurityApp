class CreateVisitors < ActiveRecord::Migration[5.0]
  def change
    create_table :visitors do |t|
      t.string :vis_id;
      t.string :name;
      t.string :phone;
      t.string :address;
      t.string :resident_id;
      t.string :purpose;

      t.timestamps
    end
  end
end
