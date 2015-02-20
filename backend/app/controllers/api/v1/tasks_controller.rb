class Api::V1::TasksController < ApplicationController
  def index
    @tasks = Task.all

    render json: @tasks
  end

  def create
    @task = Task.new(name: params[:name])

    if @task.save

    else
      render json: { errors: @task.errors }, status: 422
    end
  end

  private

  def task_params
    params.require(:task).permit(:name)
  end
end
