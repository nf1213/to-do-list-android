class Api::V1::TasksController < ApplicationController
  def index
    @tasks = Task.all

    render json: @tasks
  end

  def create
    @task = tasks.build(task_params)

    if @task.save
      render json: @task,
        status: :created,
        location: [:api, :v1, @task]
    else
      render json: { errors: @task.errors }, status: :unprocessable_entity
    end
  end

  private

  def task_params
    params.require(:task).include(:name)
  end
end
