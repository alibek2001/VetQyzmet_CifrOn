const axios = require("axios");
const cheerio = require("cheerio");
const { log } = require("console");
const fs = require("fs");

// let link = "https://kaspi.kz/shop/c/smartphones/class-apple/?page=";

// const parseSite = async () => {
//   try {
//     let arr = [];
//     let i = 82;
//     let flag = false;
//     while (true) {
//       console.log("step - ", i);

//       await axios
//         .get(link + i)
//         .then((res) => res.data)
//         .then((res) => {
//           let HTML = res;
//           $ = cheerio.load(HTML);

//           let pagination = $(`._disabled`).html();
//           //   console.log(pagination);

//           //   if (pagination !== null) {
//           //     flag = true;
//           //   }
//         })
//         .catch((err) => console.log(err));

//       if (flag) {
//         break;
//       }
//       i++;
//     }
//   } catch (error) {
//     console.log("err - ", error);
//   }
// };

// parseSite();

// const parse = async () => {
//   const getHTML = async (url) => {
//     const { data } = await axios.get(url);
//     return cheerio.load(data);
//   };

//   const $ = await getHTML(
//     "https://kaspi.kz/shop/c/smartphones/class-apple/?page=83"
//   );

//   console.log($.html());
// };

// parse();
//======================================================

const parse = async () => {
  const getHTML = async (url) => {
    const { data } = await axios.get(url);
    return cheerio.load(data);
  };

  const $ = await getHTML(
    "https://kaspi.kz/shop/c/smartphones/class-apple/?page=1"
  );

  const pageNumber = $(`a.pagination__el`).eq(-2).text();

  for (let i = 1; i <= pageNumber; i++) {
    const selector = await getHTML(
      `https://kaspi.kz/shop/c/smartphones/class-apple/?page=${i}`
    );

    selector(".item-card").each(async (i, element) => {
      const title = selector(element).find("a.item-card__name").text();
      const link = `https://kaspi.kz${selector(element)
        .find("a")
        .attr("href")}`;

      await getAllMerchantsFromProduct(getSkuFromUrl(link));
      //   fs.appendFileSync("./data.txt", `${title} ${link}\n`);
    });
  }
};

const getAllMerchantsFromProduct = async (productId) => {
  await axios
    .post(`https://kaspi.kz/yml/offer-view/offers/${productId}`, {
      cityId: "750000000",
      id: productId,
      limit: 50,
      sort: true,
    })
    .then((data) => {
      return console.log(data.data);
    });
};

const getSkuFromUrl = (string) => {
  const url = new URL(string);

  const pathname = url.pathname;
  let sku = "";

  const arr = pathname.split("/");
  for (let a of arr) {
    if (a.trim() === "") {
      continue;
    }
    const ar = a.split(/-/g);
    for (let b of ar) {
      if (b.trim() === "") {
        continue;
      }

      sku = b.replace(/\D/g, "");
    }
  }

  return sku;
};

parse();
