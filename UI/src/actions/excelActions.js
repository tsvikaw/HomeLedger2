import * as excelApi from "./excelApi";

export const READ_EXCEL_SUCCESS = "READ_EXCEL_SUCCESS";
export const READ_EXCEL_FAILURE = "READ_EXCEL_FAILURE";
export const READ_EXCEL_REQUEST = "READ_EXCEL_REQUEST";
export const READ_EXCEL_RESET = "READ_EXCEL_RESET";

export const LOAD_EXCEL_LINES_SUCCESS = "LOAD_EXCEL_LINES_SUCCESS";
export const LOAD_EXCEL_LINES_FAILURE = "LOAD_EXCEL_LINES_FAILURE";
export const FETCH_EXCEL_LINES_REQUEST = "FETCH_EXCEL_LINES_REQUEST";
export const LOAD_EXCEL_LINES_RESET = "LOAD_EXCEL_LINES_RESET";

export const SAVE_EXCEL_LINES_SUCCESS = "SAVE_EXCEL_LINES_SUCCESS";
export const SAVE_EXCEL_LINES_FAILURE = "SAVE_EXCEL_LINES_FAILURE";
export const SAVE_EXCEL_LINES_REQUEST = "SAVE_EXCEL_LINES_REQUEST";

export function saveExcelLinesSuccess(excelLines) {
  return { type: SAVE_EXCEL_LINES_SUCCESS, excelLines };
}

export function saveExcelLinesFailure(error) {
  return { type: SAVE_EXCEL_LINES_FAILURE, error};
}

export function saveExcelLinesRequest() {
  return { type: SAVE_EXCEL_LINES_REQUEST };
}

export function loadExcelLinesSuccess(excelLines) {
  return { type: LOAD_EXCEL_LINES_SUCCESS, excelLines };
}

export function loadExcelLinesFailure(error) {
  return { type: LOAD_EXCEL_LINES_FAILURE, error};
}

export function fetchExcelLinesRequest() {
  return { type: FETCH_EXCEL_LINES_REQUEST };
}

export function loadExcelLinesReset() {
  return { type: LOAD_EXCEL_LINES_RESET };
}

export function readExcelSuccess(excelContent) {
  return { type: READ_EXCEL_SUCCESS, excelContent };
}

export function readExcelFailure(error) {
  return { type: READ_EXCEL_FAILURE, error};
}

export function readExcelRequest() {
  return { type: READ_EXCEL_REQUEST };
}

export function readExcelReset() {
  return { type: READ_EXCEL_RESET };
}

export function loadExcelLines(file, sheetNumber, skipLines, dateColumn, amountColumn, descriptionColumn) {
  return function(dispatch) {
    dispatch(fetchExcelLinesRequest());
    return excelApi
      .getExcelLines(file, sheetNumber, skipLines, dateColumn, amountColumn, descriptionColumn)
      .then(excelLines => {
        dispatch(loadExcelLinesSuccess(excelLines));
      })
      .catch(error => {
        dispatch(loadExcelLinesFailure(error));
      });
  };
}

export function readExcel(file) {
  return function(dispatch) {
    dispatch(readExcelRequest());
    return excelApi
      .readExcel(file)
      .then(excelContent => {
        dispatch(readExcelSuccess(excelContent));
      })
      .catch(error => {
        dispatch(readExcelFailure(error));
      });
  };
}

export function submitExcelLines(lines) {
  return function(dispatch) {
    dispatch(saveExcelLinesRequest());
    return excelApi
      .submitExcelLines(lines)
      .then(returningExcelLines => {
        dispatch(saveExcelLinesSuccess(lines));
      })
      .catch(error => {
        dispatch(saveExcelLinesFailure(error));
      });
  };
}
