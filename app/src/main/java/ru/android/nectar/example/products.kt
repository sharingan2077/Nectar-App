package ru.android.nectar.example

import ru.android.nectar.R
import ru.android.nectar.data.local.entity.ProductEntity

val dataProductList = listOf(
    // Фрукты (5 товаров)
    ProductEntity(
        imageName = "img_fruit_banana",
        name = "Бананы органические",
        spec = "7 шт, 1 кг",
        price = "140 ₽",
        category = "хит",
        productType = "Фрукты"
    ),
    ProductEntity(
        imageName = "img_fruit_apple",
        name = "Яблоки свежие",
        spec = "500 г",
        price = "89 ₽",
        category = "хит",
        productType = "Фрукты"
    ),
    ProductEntity(
        imageName = "img_fruit_orange",
        name = "Апельсины",
        spec = "1 кг",
        price = "120 ₽",
        category = "эксклюзив",
        productType = "Фрукты"
    ),
    ProductEntity(
        imageName = "img_fruit_kiwi",
        name = "Киви",
        spec = "5 шт",
        price = "180 ₽",
        category = "экзотика",
        productType = "Фрукты"
    ),
    ProductEntity(
        imageName = "img_fruit_grape",
        name = "Виноград зеленый",
        spec = "400 г",
        price = "210 ₽",
        category = "хит",
        productType = "Фрукты"
    ),

    // Овощи (5 товаров)
    ProductEntity(
        imageName = "img_vegetable_ginger",
        name = "Имбирь",
        spec = "6 шт, 100 г",
        price = "120 ₽",
        category = "хит",
        productType = "Овощи"
    ),
    ProductEntity(
        imageName = "img_vegetable_tomato",
        name = "Помидоры черри",
        spec = "250 г",
        price = "150 ₽",
        category = "премиум",
        productType = "Овощи"
    ),
    ProductEntity(
        imageName = "img_vegetable_cucumber",
        name = "Огурцы тепличные",
        spec = "1 кг",
        price = "95 ₽",
        category = "стандарт",
        productType = "Овощи"
    ),
    ProductEntity(
        imageName = "img_vegetable_potato",
        name = "Картофель молодой",
        spec = "1 кг",
        price = "65 ₽",
        category = "эксклюзив",
        productType = "Овощи"
    ),
    ProductEntity(
        imageName = "img_vegetable_carrot",
        name = "Морковь",
        spec = "500 г",
        price = "45 ₽",
        category = "стандарт",
        productType = "Овощи"
    ),

    // Молочные продукты (5 товаров)
    ProductEntity(
        imageName = "img_milk_milk",
        name = "Молоко 3.2%",
        spec = "1 л",
        price = "85 ₽",
        category = "хит",
        productType = "Молочные продукты"
    ),
    ProductEntity(
        imageName = "img_milk_cheese",
        name = "Сыр Российский",
        spec = "200 г",
        price = "180 ₽",
        category = "премиум",
        productType = "Молочные продукты"
    ),
    ProductEntity(
        imageName = "img_milk_yogurt",
        name = "Йогурт питьевой",
        spec = "250 мл",
        price = "60 ₽",
        category = "акция",
        productType = "Молочные продукты"
    ),
    ProductEntity(
        imageName = "img_milk_butter",
        name = "Масло сливочное",
        spec = "180 г",
        price = "120 ₽",
        category = "хит",
        productType = "Молочные продукты"
    ),
    ProductEntity(
        imageName = "img_milk_kefir",
        name = "Кефир 2.5%",
        spec = "1 л",
        price = "90 ₽",
        category = "эксклюзив",
        productType = "Молочные продукты"
    ),

    // Мясо и птица (5 товаров)
    ProductEntity(
        imageName = "img_meat_chicken",
        name = "Курица охлажденная",
        spec = "1 кг",
        price = "250 ₽",
        category = "хит",
        productType = "Мясо и птица"
    ),
    ProductEntity(
        imageName = "img_meat_beef",
        name = "Говядина вырезка",
        spec = "500 г",
        price = "450 ₽",
        category = "премиум",
        productType = "Мясо и птица"
    ),
    ProductEntity(
        imageName = "img_meat_mince",
        name = "Фарш куриный",
        spec = "400 г",
        price = "180 ₽",
        category = "стандарт",
        productType = "Мясо и птица"
    ),
    ProductEntity(
        imageName = "img_meat_turkey",
        name = "Индейка филе",
        spec = "600 г",
        price = "320 ₽",
        category = "диетическое",
        productType = "Мясо и птица"
    ),
    ProductEntity(
        imageName = "img_meat_sausage",
        name = "Колбаса докторская",
        spec = "300 г",
        price = "220 ₽",
        category = "хит",
        productType = "Мясо и птица"
    ),

    // Рыба и морепродукты (5 товаров)
    ProductEntity(
        imageName = "img_fish_salmon",
        name = "Лосось свежий",
        spec = "300 г",
        price = "600 ₽",
        category = "премиум",
        productType = "Рыба и морепродукты"
    ),
    ProductEntity(
        imageName = "img_fish_shrimp",
        name = "Креветки тигровые",
        spec = "400 г",
        price = "750 ₽",
        category = "эксклюзив",
        productType = "Рыба и морепродукты"
    ),
    ProductEntity(
        imageName = "img_fish_cod",
        name = "Треска филе",
        spec = "500 г",
        price = "350 ₽",
        category = "хит",
        productType = "Рыба и морепродукты"
    ),
    ProductEntity(
        imageName = "img_fish_mussels",
        name = "Мидии в раковинах",
        spec = "1 кг",
        price = "450 ₽",
        category = "акция",
        productType = "Рыба и морепродукты"
    ),
    ProductEntity(
        imageName = "img_fish_squid",
        name = "Кальмары очищенные",
        spec = "300 г",
        price = "280 ₽",
        category = "стандарт",
        productType = "Рыба и морепродукты"
    ),

    // Бакалея (5 товаров)
    ProductEntity(
        imageName = "img_groceries_flour",
        name = "Мука пшеничная",
        spec = "1 кг",
        price = "65 ₽",
        category = "основное",
        productType = "Бакалея"
    ),
    ProductEntity(
        imageName = "img_groceries_sugar",
        name = "Сахар белый",
        spec = "1 кг",
        price = "80 ₽",
        category = "основное",
        productType = "Бакалея"
    ),
    ProductEntity(
        imageName = "img_groceries_rice",
        name = "Рис круглозерный",
        spec = "900 г",
        price = "120 ₽",
        category = "хит",
        productType = "Бакалея"
    ),
    ProductEntity(
        imageName = "img_groceries_pasta",
        name = "Макароны спагетти",
        spec = "500 г",
        price = "90 ₽",
        category = "стандарт",
        productType = "Бакалея"
    ),
    ProductEntity(
        imageName = "img_groceries_buchwheat",
        name = "Гречка ядрица",
        spec = "800 г",
        price = "110 ₽",
        category = "популярное",
        productType = "Бакалея"
    ),

    // Напитки (5 товаров)
    ProductEntity(
        imageName = "img_drink_water",
        name = "Вода минеральная",
        spec = "1,5 л",
        price = "60 ₽",
        category = "основное",
        productType = "Напитки"
    ),
    ProductEntity(
        imageName = "img_drink_juice",
        name = "Сок апельсиновый",
        spec = "1 л",
        price = "130 ₽",
        category = "хит",
        productType = "Напитки"
    ),
    ProductEntity(
        imageName = "img_drink_cola",
        name = "Кола",
        spec = "2 л",
        price = "110 ₽",
        category = "популярное",
        productType = "Напитки"
    ),
    ProductEntity(
        imageName = "img_drink_tea",
        name = "Чай черный",
        spec = "100 пакетиков",
        price = "250 ₽",
        category = "премиум",
        productType = "Напитки"
    ),
    ProductEntity(
        imageName = "img_drink_coffee",
        name = "Кофе молотый",
        spec = "250 г",
        price = "350 ₽",
        category = "хит",
        productType = "Напитки"
    ),

    // Хлеб и выпечка (5 товаров)
    ProductEntity(
        imageName = "img_bread_bread",
        name = "Хлеб бородинский",
        spec = "400 г",
        price = "55 ₽",
        category = "основное",
        productType = "Хлеб и выпечка"
    ),
    ProductEntity(
        imageName = "img_bread_bun",
        name = "Булочки сдобные",
        spec = "4 шт",
        price = "90 ₽",
        category = "хит",
        productType = "Хлеб и выпечка"
    ),
    ProductEntity(
        imageName = "img_bread_baguette",
        name = "Багет французский",
        spec = "250 г",
        price = "70 ₽",
        category = "свежее",
        productType = "Хлеб и выпечка"
    ),
    ProductEntity(
        imageName = "img_bread_cookies",
        name = "Печенье овсяное",
        spec = "300 г",
        price = "120 ₽",
        category = "к чаю",
        productType = "Хлеб и выпечка"
    ),
    ProductEntity(
        imageName = "img_bread_pie",
        name = "Пирог с яблоками",
        spec = "500 г",
        price = "200 ₽",
        category = "десерты",
        productType = "Хлеб и выпечка"
    ),

    // Замороженные продукты (5 товаров)
    ProductEntity(
        imageName = "img_cold_vegetable_mix",
        name = "Овощная смесь",
        spec = "450 г",
        price = "150 ₽",
        category = "эксклюзив",
        productType = "Замороженные продукты"
    ),
    ProductEntity(
        imageName = "img_cold_pizza",
        name = "Пицца 4 сыра",
        spec = "400 г",
        price = "300 ₽",
        category = "хит",
        productType = "Замороженные продукты"
    ),
    ProductEntity(
        imageName = "img_cold_ice_cream",
        name = "Мороженое пломбир",
        spec = "250 г",
        price = "180 ₽",
        category = "десерты",
        productType = "Замороженные продукты"
    ),
    ProductEntity(
        imageName = "img_cold_dumplings",
        name = "Пельмени классические",
        spec = "800 г",
        price = "250 ₽",
        category = "основное",
        productType = "Замороженные продукты"
    ),
    ProductEntity(
        imageName = "img_cold_berries",
        name = "Ягоды лесные",
        spec = "300 г",
        price = "220 ₽",
        category = "премиум",
        productType = "Замороженные продукты"
    ),

    // Сладости и снеки (5 товаров)
    ProductEntity(
        imageName = "img_sweet_chocolate",
        name = "Шоколад молочный",
        spec = "100 г",
        price = "90 ₽",
        category = "хит",
        productType = "Сладости и снеки"
    ),
    ProductEntity(
        imageName = "img_sweet_chips",
        name = "Чипсы сырные",
        spec = "150 г",
        price = "120 ₽",
        category = "перекус",
        productType = "Сладости и снеки"
    ),
    ProductEntity(
        imageName = "img_sweet_nuts",
        name = "Орехи грецкие",
        spec = "200 г",
        price = "250 ₽",
        category = "полезное",
        productType = "Сладости и снеки"
    ),
    ProductEntity(
        imageName = "img_sweet_dried_fruits",
        name = "Курага",
        spec = "250 г",
        price = "180 ₽",
        category = "здоровое питание",
        productType = "Сладости и снеки"
    ),
    ProductEntity(
        imageName = "img_sweet_candy",
        name = "Конфеты шоколадные",
        spec = "300 г",
        price = "200 ₽",
        category = "к чаю",
        productType = "Сладости и снеки"
    )
)