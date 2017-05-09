class CreateSecurities < ActiveRecord::Migration[5.0]
  def change
    create_table :securities do |t|
      t.string :sec_id;
      t.string :name;
      t.string :phone;

      t.timestamps
    end
  end
end
