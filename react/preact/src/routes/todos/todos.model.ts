export interface ToDosList {
    todos: ToDo[];
}

export interface ToDo {
    id?: number
    todo: string
    completed: boolean
    userId: number
}