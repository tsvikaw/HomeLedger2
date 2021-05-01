import { combineReducers } from 'redux';
import auth from './auth';
import runtime from './runtime';
import navigation from './navigation';
import queryResults from './queryResults';
import summaryResults from './summaryResults';
import pieResults from './pieResults';
import categories from './categories';
import accounts from './accounts';
import lines from './lines';
import excelLines from './excelLinesReducer';

export default combineReducers({
  auth,
  runtime,
  navigation,
  queryResults,
  summaryResults,
  pieResults,
  categories,
  accounts,
  lines,
  excelLines,
});
