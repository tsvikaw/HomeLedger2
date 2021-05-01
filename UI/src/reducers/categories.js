import {
  FETCH_CATEGORIES_REQUEST,
  FETCH_CATEGORIES_SUCCESS,
  FETCH_CATEGORIES_FAILURE,
  NEW_CATEGORY_REQUEST,
  NEW_CATEGORY_SUCCESS,
  NEW_CATEGORY_FAILURE,
  UPDATE_CATEGORY_REQUEST,
  UPDATE_CATEGORY_SUCCESS,
  UPDATE_CATEGORY_FAILURE,
  DELETE_CATEGORY_REQUEST,
  DELETE_CATEGORY_SUCCESS,
  DELETE_CATEGORY_FAILURE
} from "../actions/categories";

export default function categories(
  state = {
    isFetching: false,
    isDeleted: false
  },
  action
) {
  switch (action.type) {
    case FETCH_CATEGORIES_REQUEST:
      return Object.assign({}, state, {
        isFetching: true
      });
    case FETCH_CATEGORIES_SUCCESS:
      if (action.categories) {
        buildCategoryHierarchy(action.categories);
      }
      return Object.assign({}, state, {
        isFetching: false,
        categories: action.categories
      });
    case FETCH_CATEGORIES_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: "Something wrong happened. Please come back later"
      });
    case NEW_CATEGORY_REQUEST:
      return Object.assign({}, state, {
        isFetching: true,
        category: null
      });
    case NEW_CATEGORY_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        category: action.category
      });
    case NEW_CATEGORY_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message:
          action.message.indexOf("409") !== -1
            ? "Conflict found, cannot perform save."
            : "Something wrong happened. Please come back later"
      });
      case UPDATE_CATEGORY_REQUEST:
          return Object.assign({}, state, {
            isFetching: true,
            category: null
          });
        case UPDATE_CATEGORY_SUCCESS:
          return Object.assign({}, state, {
            isFetching: false,
            category: action.category
          });
        case UPDATE_CATEGORY_FAILURE:
          return Object.assign({}, state, {
            isFetching: false,
            message:
              action.message.indexOf("409") !== -1
                ? "Conflict found, cannot perform save."
                : "Something wrong happened. Please come back later"
          });
    case DELETE_CATEGORY_REQUEST:
      return Object.assign({}, state, {
        isFetching: true,
        isDeleted: false
      });
    case DELETE_CATEGORY_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        isDeleted: true
      });
    case DELETE_CATEGORY_FAILURE:
        return Object.assign({}, state, {
        isFetching: false,
        isDeleted: false,
        message: action.message
      });
    default:
      return state;
  }
}

function buildCategoryHierarchy(categories) {
  var dic = {};
  categories.forEach(element => {
    dic[element.id] = element;
    element.children = [];
  });
  categories.forEach(element => {
    if (element.parentId) {
      dic[element.parentId].children.push(element);
    }
  });
}
