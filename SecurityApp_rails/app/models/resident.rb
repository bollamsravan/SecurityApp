class Resident < ApplicationRecord
  belongs_to :user
  has_many :visitors

  validates_uniqueness_of :door_no
  validates_presence_of :door_no

  def get_notifications
    self.visitors.where(status: VISITOR_STATUS_NOT_ADDRESSED)
  end
  def accept_or_reject_visitor(visitor_id,acceptance)
    @visitor = Visitor.find(visitor_id)
    if @visitor
      if acceptance
        @visitor.status = VISITOR_STATUS_ACCEPTED
      else
        @visitor.status = VISITOR_STATUS_REJECTED
      end
      @visitor.save
      true
    else
      false
    end
  end
end
