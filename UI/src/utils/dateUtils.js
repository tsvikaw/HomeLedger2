import { pad } from './stringUtils';

export const formatDate = date => {
    return (
      pad(date.getDate(), 2) +
      "/" +
      pad(date.getMonth() + 1, 2) +
      "/" +
      date.getFullYear()
    );
  };