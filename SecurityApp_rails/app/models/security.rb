class Security < ApplicationRecord
  belongs_to  :user

  def add_visitor(name,phone,address,purpose,door_no)
    @resident = Resident.find_by_door_no(door_no)
    if @resident
      @visitor = Visitor.new(name: name, phone: phone, address: address, purpose: purpose)
      @visitor.resident_id = @resident.id
      @visitor.save
      return true,"saved"
    else
      return false,"Resident not found"
    end

  end
end
