import {
  FETCH_QUERY_RESULTS_REQUEST,
  FETCH_QUERY_RESULTS_SUCCESS,
  FETCH_QUERY_RESULTS_FAILURE,
} from "../actions/queryResults";

export default function queryResults(
  state = {
    isFetching: false
  },
  action
) {
  switch (action.type) {
    case FETCH_QUERY_RESULTS_REQUEST:
      return Object.assign({}, state, {
        isFetching: true
      });
    case FETCH_QUERY_RESULTS_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        queryResults: action.queryResults
      });
    case FETCH_QUERY_RESULTS_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: "Something wrong happened. Please come back later"
      });
    default:
      return state;
  }
}
