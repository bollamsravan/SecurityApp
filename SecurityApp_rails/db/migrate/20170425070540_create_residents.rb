class CreateResidents < ActiveRecord::Migration[5.0]
  def change
    create_table :residents do |t|
      t.boolean :availability, default: false
      t.string :door_no, default: ""
      t.string :residence,  default: ""
      t.timestamps
    end
  end
end
