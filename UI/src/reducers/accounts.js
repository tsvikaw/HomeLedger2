import {
  FETCH_ACCOUNTS_REQUEST,
  FETCH_ACCOUNTS_SUCCESS,
  FETCH_ACCOUNTS_FAILURE,
} from '../actions/accounts';

export default function accounts(
  state = {
    isFetching: false,
  },
  action,
) {
  switch (action.type) {
    case FETCH_ACCOUNTS_REQUEST:
      return Object.assign({}, state, {
        isFetching: true,
      });
    case FETCH_ACCOUNTS_SUCCESS:
      return Object.assign({}, state, {
        isFetching: false,
        accounts: action.accounts,
      });
    case FETCH_ACCOUNTS_FAILURE:
      return Object.assign({}, state, {
        isFetching: false,
        message: 'Something wrong happened. Please come back later',
      });
    default:
      return state;
  }
}
