class Api::V1::TasksController < ApplicationController
  def index
    @tasks = Task.all

    render json: @tasks
  end
end
