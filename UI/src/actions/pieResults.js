import { pad } from "../utils/stringUtils";
import config from "../config";

export const FETCH_PIE_RESULTS_REQUEST = "FETCH_PIE_RESULTS_REQUEST";
export const FETCH_PIE_RESULTS_SUCCESS = "FETCH_PIE_RESULTS_SUCCESS";
export const FETCH_PIE_RESULTS_FAILURE = "FETCH_PIE_RESULTS_FAILURE";

function requestFetchPieResults() {
  return {
    type: FETCH_PIE_RESULTS_SUCCESS,
    isFetching: true
  };
}

function fetchPieResultsSuccess(pieResults) {
  return {
    type: FETCH_PIE_RESULTS_SUCCESS,
    isFetching: false,
    pieResults
  };
}

function fetchPieResultsError(message) {
  return {
    type: FETCH_PIE_RESULTS_FAILURE,
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

  export function fetchPieResults(filter) {
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
      dispatch(requestFetchPieResults());
      requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
      const pieUrl = config.reportServerUrl + "lines/pie";
      return fetch(pieUrl, requestConfig)
        .then(response =>
          response.json().then(responseJson => ({
            pieResults: responseJson,
            responseJson
          }))
        )
        .then(({ pieResults, responseJson }) => {
          if (!responseJson || responseJson.error) {
            // If there was a problem, we want to
            // dispatch the error condition
  
            dispatch(fetchPieResultsError(pieResults.message));
            return Promise.reject(pieResults);
          }
          // Dispatch the success action
          //console.log(pieResults);
          dispatch(fetchPieResultsSuccess(pieResults));
          return Promise.resolve(pieResults);
        })
        .catch(err => console.error("Error: ", err));
    };
  }