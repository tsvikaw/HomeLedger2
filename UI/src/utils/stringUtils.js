export function pad(n, width, z) {
    z = z || "0";
    n = n + "";
    return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
  };

  export function numberWithCommas(x) {
    return x.toString().split('.')[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  };