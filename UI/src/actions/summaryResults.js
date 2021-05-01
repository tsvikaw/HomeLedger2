import { pad } from "../utils/stringUtils";
import config from "../config";

export const FETCH_SUMMARY_RESULTS_REQUEST = "FETCH_SUMMARY_RESULTS_REQUEST";
export const FETCH_SUMMARY_RESULTS_SUCCESS = "FETCH_SUMMARY_RESULTS_SUCCESS";
export const FETCH_SUMMARY_RESULTS_FAILURE = "FETCH_SUMMARY_RESULTS_FAILURE";

function requestFetchSummaryResults() {
  return {
    type: FETCH_SUMMARY_RESULTS_SUCCESS,
    isFetching: true
  };
}

function fetchSummaryResultsSuccess(summaryResults) {
  return {
    type: FETCH_SUMMARY_RESULTS_SUCCESS,
    isFetching: false,
    summaryResults
  };
}

function fetchSummaryResultsError(message) {
  return {
    type: FETCH_SUMMARY_RESULTS_FAILURE,
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

  export function fetchSummaryResults(filter) {
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
     //console.log(JSON.stringify(data));
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
      dispatch(requestFetchSummaryResults());
      requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
      const summaryUrl = config.reportServerUrl + "lines/summaryByPeriod?month=6";
      return fetch(summaryUrl, requestConfig)
        .then(response =>
          response.json().then(responseJson => ({
            summaryResults: responseJson,
            responseJson
          }))
        )
        .then(({ summaryResults, responseJson }) => {
          if (!responseJson || responseJson.error) {
            // If there was a problem, we want to
            // dispatch the error condition
  
            dispatch(fetchSummaryResultsError(summaryResults.message));
            return Promise.reject(summaryResults);
          }
          // Dispatch the success action
          //console.log(summaryResults);
          dispatch(fetchSummaryResultsSuccess(summaryResults));
          return Promise.resolve(summaryResults);
        })
        .catch(err => console.error("Error: ", err));
    };
  }