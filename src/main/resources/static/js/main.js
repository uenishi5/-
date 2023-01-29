const data = {
    newsapi: {
        url: "/newsapi",
        data: { // 送信データ
            q: $("#search-form-query").val(),
            page: $("#page-item-value").val(),
            size: $("#search-form-size").val(),
            date: $("#search-form-date").val()
        },
        filters: {
            size: {
                id: "#search-form-size",
                tag: [
                    $("<span>", { class: "col-1", id: "display_size" }),
                    $("<input>", {
                        class: "filter-setting-value form-range", id: "search-form-size", type: "range", value: "20",
                        min: "10", max: "100", step: "10", oninput: "$('#display_size').text($(this).val());"
                    })
                ],
            },
            date: {
                id: "#search-form-date",
                tag: [
                    $("<select>", {
                        class: "filter-setting-value form-select", id: "search-form-date"
                    })
                        .append($("<option>", { value: "TODAY", text: "今日"}).attr("selected", true))
                        .append($("<option>", { value: "THIS_WEEK", text: "今週" }))
                        .append($("<option>", { value: "THIS_MONTH", text: "今月" }))
                        .append($("<option>", { value: "THIS_YEAR", text: "今年" }))
                        .append($("<option>", { value: "ALL", text: "すべて" }))
                ]
            },
            sort:{
                id: "#search-form-sort",
                tag: [
                    $("<select>", {
                        class: "filter-setting-value form-select", id: "search-form-sort"
                    })
                        .append($("<option>", { value: "PUBLISHED_AT", text: "公開日"}).attr("selected", true))
                        .append($("<option>", { value: "POPULARITY", text: "人気" }))
                        .append($("<option>", { value: "RELEVANCY", text: "関連性" }))
                ]
            }
        }
    },
    pornhubapi: {
        url: "/pornhubapi",
        data: {
            q: $("#search-form-query").val(),
            page: $("#page-item-value").val(),
            thumbsize: $("#search-form-size").val()
        }
    }
}


$(window).ready(function () {
    $("#search-form-filter-apply").click();
    filter_setting("newsapi");
});

$("#search-form-query")
    .focusin(function () {
        $("#search-form").addClass("search-form-focusin");
    })
    .focusout(function () {
        $("#search-form").removeClass("search-form-focusin");
    });

$("#search-form-query").on("input", function () {
    if ($(this).val()) {
        $("#search-form-submit").attr("disabled", false);
        $("#search-form-reset").show();
    }
    else {
        $("#search-form-reset").click();
    }
});

$("#search-form-reset").click(function () {
    $("#search-form-submit").attr("disabled", true);
    $("#search-form-reset").hide();
});

$("#change-theme").click(function () {
    $("body").toggleClass("dark").toggleClass("light");

    if ($("body").hasClass("light")) {
        $("#search-form-query").attr("placeholder", "ITMediaで検索");
    }
    if ($("body").hasClass("dark")) {
        $("#search-form-query").attr("placeholder", "Pornhubで検索");
    }
});

$("#search-form-filter-apply").click(function () {
    $("#search-form-filter-tags").children().remove();

    $("#filterForm").find(".filter-setting").each(function () {
        $("#search-form-filter-tags")
            .append($("<div>", { "class": "col-auto" })
                .append($("<button>", { "class": "btn btn-border", "data-bs-toggle": "modal", "data-bs-target": "#filterForm" })
                    .append($("<div>", { "text": $(this).find(".col-form-label").text() }))
                    .append($("<div>", { "text": ":" }))
                    .append($("<div>", { "text": $(this).find(".filter-setting-value").val(), "name": $(this).find(".filter-setting-value").data("param-name") }))
                ))
    });

})

//絞り込みの設定
function filter_setting(e) {
    console.log(data[e].filters);
    data[e].filters.forEach(filter => {
        const filter_setting = $("<div>", { "class": "filter-setting row mb-3" })
        const label = $("<div>", { "class": "col-3" }).append($("<div>", { "class": "filter-setting-label" }));
        const value = $("<div>", { "class": "col-9" });

        filter.tag.forEach(t => value.append(t))

        filter_setting
            .append(label)
            .append(value);
    });
}

$("#search-form").on("submit", function (e) {
    const useApi = $(this)

    e.preventDefault();  // デフォルトのイベント(ページの遷移やデータ送信など)を無効にする
    $.ajax({
        url: "/youtubeapi",
        type: "POST",
        dataType: "json",
        data: {
            q: $("#search-form-query").val(),
            page: $("#page-item-value").val(),
        }
    })
        .done(function (response) {
            $("#contents").children().remove();
            response.contents.forEach(content => {
                $("#contents")
                    .append($("<button>", { "class": "item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
                        .click(function () {
                            const iconUrl = $(this).find(".iconUrl").attr("src");
                            const title = $(this).find(".title").text();
                            const publishBy = $(this).find(".publishBy").text();
                            const publishAt = $(this).find(".publishAt").text();
                            const source = $(this).find(".source").text();
                            const originalUrl = $(this).find(".originalUrl").text();


                            $("#item-window").find(".modal-title").children().remove();
                            $("#item-window").find(".modal-title").text(title);

                            $("#item-window").find(".modal-body").children().remove();
                            $("#item-window").find(".modal-body")
                                .append($("<iframe>", { "class": "w-100", "src": source }));

                            $("#item-window").find(".modal-footer").children().remove();
                            $("#item-window").find(".modal-footer")
                                .append($("<a>", { "class": "btn ms-auto", "text": "Original", "href": originalUrl }));

                        })
                        .append($("<img>", { "class": "iconUrl", "src": content.iconUrl }))
                        .append($("<div>", { "class": "title", "text": content.title }))
                        .append($("<div>", { "class": "publishBy", "text": content.publishBy }))
                        .append($("<div>", { "class": "publishAt", "text": content.publishAt }))
                        .append($("<div>", { "class": "source", "text": content.source }))
                        .append($("<div>", { "class": "originalUrl", "text": content.originalUrl })));
            });
        })
        .fail(function (response) {
            alert("なにかしらのエラー!");  // 通信に失敗した場合の処理
        })
        .always(function (response) {

        })
});