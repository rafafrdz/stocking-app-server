# Stocking App (Http4s Server Reference Implementation)

**Project Status:** WIP

This is a reference implementation about how I think a http4s server should be implemented using tagless final.

This repo has been so influenced by the book **[Practical FP in Scala](https://www.blurb.es/b/10849946-practical-fp-in-scala-a-hands-on-approach-2nd-edi) by Gabriel Volpe**, which I strongly recommended.

This project was born from the idea of getting a homogenized and straightforward way about which patterns I think there should be used and how their implementations should be developed for creating an app using http4s. The main concept is to use _Tagless Final_ encoded as a design pattern to create algebras that abstract us about what the app should do. And finally, I explain how to create relationships between those algebras using their implementations to get a correct end-to-end.

Gabriel Volpe uses tagless final too in his book, but sometimes he defines some patterns that can confuse the reader. However, these can be seen as algebras too, which allows us to follow our main idea.

I explain in more or less detail this approach with a lot of graphs in the following sections. However, I am concerned about the business model implementation is probably not the best-fitted implementation (I don't have much experience in DDD) but I consider it fits well with our purpose and clarify in a good way.

**Friendly Reminder (10/04/2023)**: At the moment I have no clear how I could implement a better chance to handler error / message from server to out.

## Technical stack

In this repo, I try to develop a http4s server creating algebras using the tagless final encoding and the following stack:

* **cats**: basic functional blocks. From typeclasses such as Functor, Monad, and so on. to syntax and instances for some datatypes and monad transformers.
* **cats-effect**: concurrency and functional effects. It ships the default IO monad.
* **http4s**: purely functional HTTP server and client, built on top of **fs2** and **cats**.
* **circe**: standard JSON library to create encoders and decoders. It has a grateful integration with **cats**.
* **doobie**: A generic and functional library for creating JDBC connections.
* **newtype**: For creating newtypes in Scala with no runtime overhead using macros.

## Getting Started

### Deploy docker

The following command deploys a **PostgresSQL** with our business tables already created.


```bash
docker compose -f docker/docker-compose.yml up -d
```

You can connect to this Postgres instance using the following configuration:

* **host**: `localhost` (default)
* **port**: `5432` (default)
* **database**: `stockingapp`
* **user**: `admin`
* **pass**: `1234`

### Deploy server

The following command deploys the stocking app api-server. It will be listening in `localhost:8080`.

```bash
sbt run
```

### Playing with it

You can use the following sentences in order to interact with the stocking app api-server.

**List all items**
```bash
curl -i http://localhost:8080/items
```

**Create item**
```bash
curl -d '{"name":"manzana", "description":"red apple", "price":1.42}' -H "Content-Type: application/json" -X POST http://localhost:8080/items/create
```

**Find item by id**
```bash
curl -i http://localhost:8080/items/findById?id=<item_id>
```

**Find item by name**
```bash
curl -i http://localhost:8080/items/findByName?name=<item_name> 
```

**Edit item**

At this moment, it is possible just to edit the price.

```bash
curl -d '{"price":1.67}' -H "Content-Type: application/json" -X POST http://localhost:8080/items/edit?id=<item_id> 
```

**Delete item**

```bash
curl -X PUT http://localhost:8080/items/delete?id=<item_id>  
```

## Code Architecture

For this project, I have tried to follow few examples made by community and create a 'canonical' way of functional implementation. The 'way' consists in:

* **Modularize** the different steps or layers of the app-server: httsp(s), correctness construction of business model, requesting to db instances to get data, casting data to data access object (a.k.a DAO), and so on.
* **Separate** the syntaxes' or expressions' every layer with respect their semantic or implementation.
* **Relate** each layer with next one using their implementations.

### Server Semantic Architecture

![server-pipeline](doc/images/server-pipeline.svg)

### Server Code Implementation Architecture

![server-code-pipeline](doc/images/server-code-pipeline.svg)

## References

* Typed Tagless Final Interpreters. Oleg Kiselyov. https://okmij.org/ftp/tagless-final/course/lecture.pdf
* Practical FP in Scala. Gabriel Volpe. https://www.blurb.es/b/10849946-practical-fp-in-scala-a-hands-on-approach-2nd-edi
* Category Theory for Programmers (Scala Edition). Bartosz Milewski. https://www.blurb.com/b/9603882-category-theory-for-programmers-scala-edition-pape
* Tagless Final in Scala. Daniel Ciocîrlan. https://blog.rockthejvm.com/tagless-final/
* Introduction to Tagless Final. Vasiliy Kevroletin. https://serokell.io/blog/introduction-tagless-final

## Documentation

* Cats. https://typelevel.org/cats/
* Cats Effect. https://typelevel.org/cats-effect/docs/getting-started
* Http4s. https://http4s.org/v0.23/docs/quickstart.html
* Circe. https://circe.github.io/circe/
* Doobie. https://tpolecat.github.io/doobie/index.html
