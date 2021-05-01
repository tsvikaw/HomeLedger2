import {LOAD_EXCEL_LINES_SUCCESS, FETCH_EXCEL_LINES_REQUEST, LOAD_EXCEL_LINES_FAILURE,
  SAVE_EXCEL_LINES_FAILURE, LOAD_EXCEL_LINES_RESET, SAVE_EXCEL_LINES_SUCCESS, SAVE_EXCEL_LINES_REQUEST,
  READ_EXCEL_SUCCESS, READ_EXCEL_FAILURE, READ_EXCEL_REQUEST, READ_EXCEL_RESET,
} from "../actions/excelActions";


export default function excelLines(
  state = {
    isFetching: false
  },
  action
) {
  switch (action.type) {
    case FETCH_EXCEL_LINES_REQUEST:
      return Object.assign({}, state, {
        isFetching: true
      });
    case LOAD_EXCEL_LINES_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        excelLines: action.excelLines
      });
    case LOAD_EXCEL_LINES_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: action.error instanceof TypeError ? "Communication error" : "Loading Excel file has failed please validate it is a standard Excel file"
      });
      case SAVE_EXCEL_LINES_REQUEST:
      return Object.assign({}, state, {
        isFetching: true
      }); 
      case LOAD_EXCEL_LINES_RESET:  
        return Object.assign({}, state, {
          excelLines: [],
          message: ""
        });
    case SAVE_EXCEL_LINES_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        savedExcelLines: action.excelLines
      });
    case SAVE_EXCEL_LINES_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: action.error instanceof TypeError ? "Communication error" : ("Importing has failed. " + action.error) 
      });
      case READ_EXCEL_REQUEST:
        return Object.assign({}, state, {
          isFetching: true
        });
      case READ_EXCEL_SUCCESS:
        return Object.assign({}, state, {
          isFetching: false,
          excelContent: action.excelContent
        });
      case READ_EXCEL_FAILURE:
        return Object.assign({}, state, {
          isFetching: false,
          message: action.error instanceof TypeError ? "Communication error" : "Loading Excel file has failed please validate it is a standard Excel file"
        });
      case READ_EXCEL_RESET:
        return Object.assign({}, state, {
          excelContent: [],
          message: ""
        });
    default:
      return state;
  }
}