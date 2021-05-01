import {
  FETCH_SUMMARY_RESULTS_REQUEST,
  FETCH_SUMMARY_RESULTS_SUCCESS,
  FETCH_SUMMARY_RESULTS_FAILURE
} from "../actions/summaryResults";

export default function summaryResults(
  state = {
    isFetching: false
  },
  action
) {
  switch (action.type) {
    case FETCH_SUMMARY_RESULTS_REQUEST:
      return Object.assign({}, state, {
        isFetching: true
      });
    case FETCH_SUMMARY_RESULTS_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        summaryResults: action.summaryResults
      });
    case FETCH_SUMMARY_RESULTS_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: "Something wrong happened. Please come back later"
      });
    default:
      return state;
  }
}
