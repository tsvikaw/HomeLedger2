export const getCategoryImage = categoryName => {
  let glyphicon = "glyphicon glyphicon-";
  let fa = "fa fa-";
  switch (categoryName) {
    case "kupat holim":
      return glyphicon + "heart-empty";
    case "kindergarden":
      return fa + "child";
    case "cash miscellenious":
      return glyphicon + "usd";
    case "commisions":
      return fa + "money";
    case "large supermarket":
      return glyphicon + "shopping-cart";
    case "fuel":
      return glyphicon + "road";
    case "eating out":
      return fa + "cutlery";
    case "home":
      return glyphicon + "home";
    case "cellular communication":
      return fa + "mobile-phone";
    case "technology and computer":
      return fa + "desktop";
    case "car":
      return fa + "car";
    case "pharm":
      return fa + "flask";
    case "power":
        return fa + "flash";
    case "gifts":
          return fa + "gift";
    case "cables and internet":
            return fa + "cloud";
    case "water":
            return fa + "tint";
    case "city tax":
              return fa + "flag checkered";
    default:
      return glyphicon + "folder-open";
  }
};

export const getAccountImage = account => {
  let fa = "fa fa-";
  switch (account) {
    case "Bank transactions":
      return fa + "bank";
    case "Credit Card Einat":
    case "Credit card Tsvika":
      return fa + "credit-card";
    default:
      return fa + "folder-open";
  }
};
