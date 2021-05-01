import { handleResponse, handleError } from "./apiUtils";
import config from "../config";

const baseUrl = config.importServerUrl + "import";

export function getExcelLines(file, sheetNumber, skipLines, dateColumn, amountColumn, descriptionColumn) {
  let formatParameters = skipLines === -1 ? "" : 
    "/" +  sheetNumber + 
    "/" +  skipLines + 
    "/" +  dateColumn + 
    "/" +  amountColumn + 
    "/" + descriptionColumn
  return fetch(baseUrl + "/parse" + formatParameters + "?file=" + file)
    .then(handleResponse)
    .catch(handleError);
}

export function readExcel(file) {
  return fetch(baseUrl + "/read?file=" + file)
    .then(handleResponse)
    .catch(handleError);
}

export function submitExcelLines(lines) {
  return fetch( baseUrl, {
    method: 'post',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(lines)
  })
    .then(handleResponse)
    .catch(handleError);
}