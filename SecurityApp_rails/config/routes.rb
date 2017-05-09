Rails.application.routes.draw do
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  post 'authenticate', to: 'authentication#authenticate'
  post 'resident/register'
  post 'security/add_visitor'
  post 'resident/get_visitor_notifications'
  post 'resident/accept_or_reject_visitor'
  post 'resident/update'
  post 'resident/availability'
  post 'resident/set_availability'
  post 'resident/resident_availability'
  post 'resident/get_notifications'

  post 'security/register'
  post 'security/update'
end
