import {
  FETCH_PIE_RESULTS_REQUEST,
  FETCH_PIE_RESULTS_SUCCESS,
  FETCH_PIE_RESULTS_FAILURE
} from "../actions/pieResults";

export default function pieResults(
  state = {
    isFetching: false
  },
  action
) {
  switch (action.type) {
    case FETCH_PIE_RESULTS_REQUEST:
      return Object.assign({}, state, {
        isFetching: true
      });
    case FETCH_PIE_RESULTS_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        pieResults: action.pieResults
      });
    case FETCH_PIE_RESULTS_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: "Something wrong happened. Please come back later"
      });
    default:
      return state;
  }
}
