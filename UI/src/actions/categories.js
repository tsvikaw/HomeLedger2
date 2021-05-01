import config from "../config";

export const FETCH_CATEGORIES_REQUEST = "FETCH_CATEGORIES_REQUEST";
export const FETCH_CATEGORIES_SUCCESS = "FETCH_CATEGORIES_SUCCESS";
export const FETCH_CATEGORIES_FAILURE = "FETCH_CATEGORIES_FAILURE";

export const NEW_CATEGORY_REQUEST = "NEW_CATEGORY_REQUEST";
export const NEW_CATEGORY_SUCCESS = "NEW_CATEGORY_SUCCESS";
export const NEW_CATEGORY_FAILURE = "NEW_CATEGORY_FAILURE";

export const UPDATE_CATEGORY_REQUEST = "UPDATE_CATEGORY_REQUEST";
export const UPDATE_CATEGORY_SUCCESS = "UPDATE_CATEGORY_SUCCESS";
export const UPDATE_CATEGORY_FAILURE = "UPDATE_CATEGORY_FAILURE";

export const DELETE_CATEGORY_REQUEST = "DELETE_CATEGORY_REQUEST";
export const DELETE_CATEGORY_SUCCESS = "DELETE_CATEGORY_SUCCESS";
export const DELETE_CATEGORY_FAILURE = "DELETE_CATEGORY_FAILURE";

function requestFetchCategories() {
  return {
    type: FETCH_CATEGORIES_SUCCESS,
    isFetching: true
  };
}

function fetchCategoriesSuccess(categories) {
  return {
    type: FETCH_CATEGORIES_SUCCESS,
    isFetching: false,
    categories
  };
}

function fetchCategoriesError(message) {
  return {
    type: FETCH_CATEGORIES_FAILURE,
    isFetching: false,
    message
  };
}

function requestNewCategory() {
  return {
    type: NEW_CATEGORY_REQUEST,
    isFetching: true
  };
}

function newCategorySuccess(category) {
  return {
    type: NEW_CATEGORY_SUCCESS,
    isFetching: false,
    category
  };
}

function newCategoryError(message) {
  return {
    type: NEW_CATEGORY_FAILURE,
    isFetching: false,
    message
  };
}

function requestUpdateCategory() {
  return {
    type: UPDATE_CATEGORY_REQUEST,
    isFetching: true
  };
}

function updateCategorySuccess(category) {
  return {
    type: UPDATE_CATEGORY_SUCCESS,
    isFetching: false,
    category
  };
}

function updateCategoryError(message) {
  return {
    type: UPDATE_CATEGORY_FAILURE,
    isFetching: false,
    message
  };
}

function requestDeleteCategory() {
  return {
    type: DELETE_CATEGORY_REQUEST,
    isFetching: true
  };
}

function deleteCategorySuccess() {
  return {
    type: DELETE_CATEGORY_SUCCESS,
    isFetching: false
  };
}

function deleteCategoryError(message) {
  return {
    type: DELETE_CATEGORY_FAILURE,
    isFetching: false,
    message
  };
}

export function fetchCategories(filter) {
  const requestConfig = {
    method: "get",
    headers: commonHeader
  };

  return dispatch => {
    dispatch(requestFetchCategories());
    requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
    const queryUrl = config.reportServerUrl + "categories";
    return fetch(queryUrl, requestConfig)
      .then(response =>
        response.json().then(responseJson => ({
          categories: responseJson,
          responseJson
        }))
      )
      .then(({ categories, responseJson }) => {
        if (!responseJson) {
          dispatch(fetchCategoriesError(categories.message));
          return Promise.reject(categories);
        }
        dispatch(fetchCategoriesSuccess(categories));
        return Promise.resolve(categories);
      })
      .catch(err => console.error("Error: ", err));
  };
}

const commonHeader = {
  Accept: "application/json",
  "Content-Type": "application/json",
  Authorization: "Bearer to be overridden"
};

export function newCategory(categoryCreationObject) {
  const requestConfig = {
    method: "post",
    headers: commonHeader,
    body: JSON.stringify({
      name: categoryCreationObject.name,
      description: categoryCreationObject.description,
      parentId: categoryCreationObject.parentId
    })
  };

  return dispatch => {
    dispatch(requestNewCategory());
    if (process.env.NODE_ENV === "development") {
      requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
      const queryUrl = config.updateServerUrl + "categories";

      // console.log(requestConfig.body);

      return fetch(queryUrl, requestConfig)
        .then(response =>
          response.json().then(category => ({ category, response }))
        )
        .then(({ category, response }) => {
          if (!response.ok) {
            dispatch(newCategoryError(category.message));
            return Promise.reject(category);
          }
          // console.log(category);
          dispatch(newCategorySuccess(category));
          return Promise.resolve(category);
        })
        .catch(err => console.error("Error: ", err));
    } else {
      dispatch(newCategoryError(""));
      return Promise.reject();
    }
  };
}

export function updateCategory(category) {
  const requestConfig = {
    method: "put",
    headers: commonHeader,
    body: JSON.stringify(category)
  };

  return dispatch => {
    dispatch(requestUpdateCategory());
    if (process.env.NODE_ENV === "development") {
      requestConfig.headers.Authorization = "Bearer " + localStorage.getItem("id_token");
      const queryUrl = config.updateServerUrl + "categories/" + category.id;

      // console.log(requestConfig.body);

      return fetch(queryUrl, requestConfig)
        .then(response =>
          response.json().then(category => ({ category, response }))
        )
        .then(({ category, response }) => {
          if (!response.ok) {
            dispatch(updateCategoryError(category.message));
            return Promise.reject(category);
          }
          // console.log(category);
          dispatch(updateCategorySuccess(category));
          return Promise.resolve(category);
        })
        .catch(err => console.error("Error: ", err));
    } else {
      dispatch(newCategoryError(""));
      return Promise.reject();
    }
  };
}

export function deleteCategory(id) {
  const requestConfig = {
    method: "delete",
    headers: commonHeader
  };

  return dispatch => {
    dispatch(requestDeleteCategory());
    if (process.env.NODE_ENV === "development") {
      requestConfig.headers.Authorization =
        "Bearer " + localStorage.getItem("id_token");
      const queryUrl = config.updateServerUrl + "categories/" + id;

      // console.log(requestConfig.body);

      return fetch(queryUrl, requestConfig)
        .then(response =>
          {
            // console.log(response);
            if (!response.ok) {
              dispatch(deleteCategoryError('Error occured while deleting. Please check if this category has children or lines directly.'));
            } else {
              dispatch(deleteCategorySuccess());
            }
          });
    } else {
      dispatch(newCategoryError(""));
      return Promise.reject();
    }
  };
}
