import {
    UPDATE_LINES_REQUEST,
    UPDATE_LINES_SUCCESS,
    UPDATE_LINES_FAILURE,
  } from '../actions/lines';
  
  export default function lines(
    state = {
      isFetching: false,
    },
    action,
  ) {
    switch (action.type) {
      case UPDATE_LINES_REQUEST:
        return Object.assign({}, state, {
          isFetching: true,
        });
      case UPDATE_LINES_SUCCESS:
        return Object.assign({}, state, {
          isFetching: false,
          lines: action.lines,
          message: 'Lines update successfully',
        });
      case UPDATE_LINES_FAILURE:
        return Object.assign({}, state, {
          isFetching: false,
          message:
            'Lines update failed',
        });
      default:
        return state;
    }
  }
  