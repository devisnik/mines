export const BOARD_OPEN_CELL = 'BOARD_OPEN_CELL'
export const BOARD_FLAG_CELL = 'BOARD_FLAG_CELL'
export const CLOCK_TICK = 'CLOCK_TICK'
export const ROBOT_TICK = 'ROBOT_TICK'

export function flagCell(x,y) {
    return {
        type: BOARD_FLAG_CELL,
        cell: { x, y }
    };
}
export function openCell(x,y) {
    return {
        type: BOARD_OPEN_CELL,
        cell: { x, y }
    };
}
export function clockTick() {
    return {
        type: CLOCK_TICK
    };
}
export function robotTick() {
    return {
        type: ROBOT_TICK
    };
}
