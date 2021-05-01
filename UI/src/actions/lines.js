import config from "../config";

export const UPDATE_LINES_REQUEST = 'UPDATE_LINES_REQUEST';
export const UPDATE_LINES_SUCCESS = 'UPDATE_LINES_SUCCESS';
export const UPDATE_LINES_FAILURE = 'UPDATE_LINES_FAILURE';

function requestUpdateLines(lines) {
  return {
    type: UPDATE_LINES_REQUEST,
    isFetching: true,
    lines,
  };
}

function updateLinesSuccess(lines) {
  return {
    type: UPDATE_LINES_SUCCESS,
    isFetching: false,
    lines,
  };
}

function updateLinesError(message) {
  return {
    type: UPDATE_LINES_FAILURE,
    isFetching: false,
    message,
  };
}

export function updateLines(categoryId, lines) {
  const requestConfig = {
    method: "put",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: "Bearer to be overridden"
    },
    body: JSON.stringify(
        {
        categoryId: categoryId,
        lineIds: lines
    })
  };

  return dispatch => {

    // We dispatch requestUpdateLine to kickoff the call to the API
    dispatch(requestUpdateLines([]));
    if(process.env.NODE_ENV === "development") {
    requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
    const queryUrl = config.updateServerUrl + "lines/updatecategory";

    //console.log(requestConfig.body);

    return fetch(queryUrl, requestConfig)
      .then(response => response.json().then(lines => ({ lines, response })))
      .then(({ lines, response }) => {
        if (!response.ok) {
          // If there was a problem, we want to
          // dispatch the error condition
          dispatch(updateLinesError(lines.message));
          return Promise.reject(lines);
        }
        // Dispatch the success action
        dispatch(updateLinesSuccess(lines.lineIds));
        return Promise.resolve(lines);
      })
      .catch(err => console.error('Error: ', err));
    } else {
      dispatch(updateLinesError(''));
      return Promise.reject();
    }
  };
}
