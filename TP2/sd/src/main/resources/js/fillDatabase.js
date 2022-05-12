function fillDB() {
    const Http = new XMLHttpRequest()
    const url = "127.0.0.1:8080/test/teams"

    var json = JSON.stringify({
        name: "Sporting",
    })

    Http.open("POST", url)
    Http.setRequestHeader("Content-Type", "application/json")
    Http.send(json)
}
