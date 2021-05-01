import { pad } from "../utils/stringUtils";
import config from "../config";

export const FETCH_QUERY_RESULTS_REQUEST = "FETCH_QUERY_RESULTS_REQUEST";
export const FETCH_QUERY_RESULTS_SUCCESS = "FETCH_QUERY_RESULTS_SUCCESS";
export const FETCH_QUERY_RESULTS_FAILURE = "FETCH_QUERY_RESULTS_FAILURE";

function requestFetchQueryResults() {
  return {
    type: FETCH_QUERY_RESULTS_SUCCESS,
    isFetching: true
  };
}

function fetchQueryResultsSuccess(queryResults) {
  return {
    type: FETCH_QUERY_RESULTS_SUCCESS,
    isFetching: false,
    queryResults
  };
}

function fetchQueryResultsError(message) {
  return {
    type: FETCH_QUERY_RESULTS_FAILURE,
    isFetching: false,
    message
  };
}

function getStartDate(filter){
    return (filter && filter.startYear ? filter.startYear : new Date().getFullYear()) +
    "-" +
    (filter && filter.startMonth ? pad(filter.startMonth, 2) : "01") +
    "-01T00:00:00.0+0000";
}

function getEndDate(filter){
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    let lastDayOfMonth = new Date(today.getFullYear(), today.getMonth()+1, 0);
    
    let wasEndDateSelected = !(filter && filter.endMonth === pad(today.getMonth() +1, 2) && filter.endYear === today.getFullYear);

    return (filter && wasEndDateSelected && filter.endYear ? filter.endYear: tomorrow.getFullYear()) +
    "-" +
    (filter && wasEndDateSelected && filter.endMonth ? pad(filter.endMonth, 2) : pad(tomorrow.getMonth() + 1 ,2)) +
    "-" +
    (filter && wasEndDateSelected && filter.endMonth ? lastDayOfMonth.getDate(): pad(tomorrow.getDate(),2))  +
     "T00:00:00.0+0000";
}

export function fetchQueryResults(filter) {
  let data = {
    startDate: getStartDate(filter),
    endDate: getEndDate(filter),
    account: filter && filter.account,
    description: filter && filter.description,
    categories: filter && filter.categoryIds ? filter.categoryIds : [],
    pageSize: filter && filter.pageSize,
    pageNumber: filter && filter.pageNumber - 1,
    sortBy: filter && filter.sortBy,
    sortAsc: filter && filter.sortAsc,
    startAmount: filter && filter.startAmount,
    endAmount: filter && filter.endAmount,
  };
  // console.log(data);
  const requestConfig = {
    method: "post",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: "Bearer to be overridden"
    },
    body: JSON.stringify(data)
  };

  return dispatch => {
    dispatch(requestFetchQueryResults());
    requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
    const queryUrl = config.reportServerUrl + "lines/query";
    return fetch(queryUrl, requestConfig)
      .then(response =>
        response.json().then(responseJson => ({
          queryResults: responseJson,
          responseJson
        }))
      )
      .then(({ queryResults, responseJson }) => {
        if (!responseJson || responseJson.error) {
          // If there was a problem, we want to
          // dispatch the error condition

          dispatch(fetchQueryResultsError(queryResults.message));
          return Promise.reject(queryResults);
        }
        // Dispatch the success action
        //console.log(queryResults);
        dispatch(fetchQueryResultsSuccess(queryResults));
        return Promise.resolve(queryResults);
      })
      .catch(err => console.error("Error: ", err));
  };
}