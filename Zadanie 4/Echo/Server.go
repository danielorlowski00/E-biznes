package main

import (
	"github.com/labstack/echo/v4"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

type Product struct {
	ID    uint
	Name  string  `json:"name" xml:"name" form:"name" query:"name"`
	Price float64 `json:"price" xml:"price" form:"price" query:"price"`
	gorm.Model
}

func main() {
	db, err := gorm.Open(sqlite.Open("database.db"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	db.AutoMigrate(&Product{})
	e := echo.New()

	e.GET("/getProducts", func(c echo.Context) error {
		var products []Product
		db.Find(&products)
		return c.JSON(http.StatusOK, products)
	})

	e.GET("/getProductById", func(c echo.Context) error {
		var product Product
		err := db.First(&product, c.QueryParam("ID"))
		if err.Error != nil {
			return c.JSON(http.StatusBadRequest, err)
		}
		return c.JSON(http.StatusOK, product)
	})

	e.POST("/addProduct", func(c echo.Context) error {
		name := c.QueryParam("name")
		price, _ := strconv.ParseFloat(c.QueryParam("price"), 32)
		product := Product{Name: name, Price: price}
		db.Create(&product)
		return c.JSON(http.StatusOK, product)
	})

	e.DELETE("/deleteProduct", func(c echo.Context) error {
		var product Product
		id := c.QueryParam("ID")
		err := db.First(&product, c.QueryParam("ID"))
		if err.Error != nil {
			return c.JSON(http.StatusBadRequest, err)
		}
		err = db.Delete(&Product{}, id)
		if err.Error != nil {
			return c.JSON(http.StatusBadRequest, err)
		}
		return c.JSON(http.StatusOK, product)
	})

	e.PUT("/updateProduct", func(c echo.Context) error {
		var product Product
		err := db.First(&product, c.QueryParam("ID"))
		if err.Error != nil {
			return c.JSON(http.StatusBadRequest, err)
		}
		product.Name = c.QueryParam("name")
		product.Price, _ = strconv.ParseFloat(c.QueryParam("price"), 32)
		db.Save(&product)
		return c.JSON(http.StatusOK, product)
	})

	e.Logger.Fatal(e.Start(":8080"))
}
