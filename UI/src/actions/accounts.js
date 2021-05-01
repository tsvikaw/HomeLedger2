import config from "../config";

export const FETCH_ACCOUNTS_REQUEST = "FETCH_ACCOUNTS_REQUEST";
export const FETCH_ACCOUNTS_SUCCESS = "FETCH_ACCOUNTS_SUCCESS";
export const FETCH_ACCOUNTS_FAILURE = "FETCH_ACCOUNTS_FAILURE";

function requestFetchAccounts() {
  return {
    type: FETCH_ACCOUNTS_SUCCESS,
    isFetching: true
  };
}

function fetchAccountsSuccess(accounts) {
  return {
    type: FETCH_ACCOUNTS_SUCCESS,
    isFetching: false,
    accounts
  };
}

function fetchAccountsError(message) {
  return {
    type: FETCH_ACCOUNTS_FAILURE,
    isFetching: false,
    message
  };
}

export function fetchAccounts(filter) {
  const requestConfig = {
    method: "get",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: "Bearer to be overridden"
    }
  };

  return dispatch => {
    dispatch(requestFetchAccounts());
    requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
    const queryUrl = config.reportServerUrl + "lines/accounts";
    return fetch(queryUrl, requestConfig)
      .then(response =>
        response.json().then(responseJson => ({
          accounts: responseJson,
          responseJson
        }))
      )
      .then(({ accounts, responseJson }) => {
        if (!responseJson) {
          // If there was a problem, we want to
          // dispatch the error condition
          dispatch(fetchAccountsError(accounts.message));
          return Promise.reject(accounts);
        }
        // Dispatch the success action
        dispatch(fetchAccountsSuccess(accounts));
        return Promise.resolve(accounts);
      })
      .catch(err => console.error("Error: ", err));
  };
}
