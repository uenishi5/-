const modal_title = $("#item-window .modal-title");
const modal_body = $("#item-window .modal-body");
const modal_footer = $("#item-window .modal-footer");

const date = {
    id: "#search-form-date",
    label: "日付絞り込み",
    tag: [
        $("<select>", {
            class: "filter-setting-value form-select", id: "search-form-date"
        })
            .append($("<option>", { value: "TODAY", text: "今日" }).attr("selected", true))
            .append($("<option>", { value: "THIS_WEEK", text: "今週" }))
            .append($("<option>", { value: "THIS_MONTH", text: "今月" }))
            .append($("<option>", { value: "THIS_YEAR", text: "今年" }))
            .append($("<option>", { value: "ALL", text: "すべて" }))
    ]
};

const data = {
    newsapi: {
        placeholder: "ITMediaで検索", sname: "newsapi",
        theme_color: "aqua",
        filters: [
            {
                id: "#search-form-size",
                label: "ページサイズ",
                tag: [
                    $("<span>", { class: "col-1", id: "display_size" }),
                    $("<input>", {
                        class: "filter-setting-value form-range", id: "search-form-size", type: "range", value: "20",
                        min: "10", max: "100", step: "10", oninput: "$('#display_size').text($(this).val());"
                    })
                ],
            },
            {
                id: "#search-form-sort",
                label: "並び順",
                tag: [
                    $("<select>", {
                        class: "filter-setting-value form-select", id: "search-form-sort"
                    })
                        .append($("<option>", { value: "PUBLISHED_AT", text: "公開日" }).attr("selected", true))
                        .append($("<option>", { value: "POPULARITY", text: "人気" }))
                        .append($("<option>", { value: "RELEVANCY", text: "関連性" }))
                ]
            },
            {
                id: "#search-form-language",
                label: "言語",
                tag: [
                    $("<select>", {
                        class: "filter-setting-value form-select", id: "search-form-language"
                    })
                        .append($("<option>", { value: "AR", text: "アラビア語" }))
                        .append($("<option>", { value: "DE", text: "ドイツ語" }))
                        .append($("<option>", { value: "EN", text: "英語" }).attr("selected", true))
                        .append($("<option>", { value: "ES", text: "スペイン語" }))
                        .append($("<option>", { value: "FR", text: "フランス語" }))
                        .append($("<option>", { value: "HE", text: "英語" }))
                        .append($("<option>", { value: "IT", text: "イタリア語" }))
                        .append($("<option>", { value: "NL", text: "オランダ語" }))
                        .append($("<option>", { value: "NO", text: "ノルウェー語" }))
                        .append($("<option>", { value: "PT", text: "ポルトガル語" }))
                        .append($("<option>", { value: "RU", text: "ロシア語" }))
                        .append($("<option>", { value: "SV", text: "スウェーデン語" }))
                        .append($("<option>", { value: "ZH", text: "中国語" }))
                ]
            },
            {
                id: "#search-form-searchIns",
                label: "検索対象",
                tag: [
                    $("<select>", {
                        class: "filter-setting-value form-select", id: "search-form-searchIns", "multiple": ""
                    })
                        .append($("<option>", { value: "CONTENT", text: "コンテンツ" }).attr("selected", true))
                        .append($("<option>", { value: "DESCRIPTION", text: "ディスクリプション" }).attr("selected", true))
                        .append($("<option>", { value: "TITLE", text: "タイトル" }).attr("selected", true))
                ]
            }
        ],
        ajax: function () {
            $.ajax({
                url: "/newsapi",
                type: "POST",
                dataType: "json",
                data: {
                    q: $("#search-form-query").val(),
                    page: $("#page-item-value").val(),
                    size: $("#search-form-size").val(),
                    date: $("#search-form-date").val(),
                    sort: $("#search-form-sort").val(),
                    language: $("#search-form-language").val(),
                    searchIns: $("#search-form-searchIns").val()
                }
            })
                .done(function (response) {
                    $("#contents").children().remove();
                    response.contents.forEach(content => {
                        $("#contents")
                            .append($("<button>", { "class": "col item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
                                .click(function () {
                                    modal_title.children().remove();
                                    modal_title.text(content.title);

                                    modal_body.children().remove();
                                    modal_body
                                        .append($("<iframe>", { "class": "w-100", "src": content.source }));

                                    modal_footer.children().remove();
                                    modal_footer
                                        .append($("<a>", { "class": "btn ms-auto", "text": "Original", "href": content.originalUrl }));
                                })
                                .append($("<img>", { "class": "iconUrl", "src": content.iconUrl }))
                                .append($("<div>", { "class": "title", "text": content.title }))
                                .append($("<div>", { "class": "publishBy", "text": content.publishBy }))
                                .append($("<div>", { "class": "publishAt", "text": content.publishAt }))
                                .append($("<div>", { "class": "source", "text": content.source }))
                                .append($("<div>", { "class": "originalUrl", "text": content.originalUrl })));
                    });
                })
                .fail(function (response) { })
                .always(function (response) { })
        }
    },
    pornhubapi: {
        placeholder: "Pornhubで検索", sname: "pornhubapi",
        theme_color: "dark",
        filters: [],
        ajax: function () {
            $.ajax({
                url: "/pornhubapi",
                type: "POST",
                dataType: "json",
                data: {
                    q: $("#search-form-query").val(),
                    page: $("#page-item-value").val(),
                    thumbsize: $("#search-form-size").val()
                }
            })
                .done(function (response) {
                    $("#contents").children().remove();
                    response.contents.forEach(content => {
                        $("#contents")
                            .append($("<button>", { "class": "col item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
                                .click(function () {
                                    modal_title.children().remove();
                                    modal_title.text(content.title);

                                    modal_body.children().remove();
                                    modal_body
                                        .append($("<iframe>", { "class": "w-100", "src": content.source }));

                                    modal_footer.children().remove();
                                    modal_footer
                                        .append($("<a>", { "class": "btn", "text": "Download", "href": getVideoId("viewkey=", false, content.originalUrl)}))
                                        .append($("<a>", { "class": "btn ms-auto", "text": "Original", "href": content.originalUrl }));
                                })
                                .append($("<img>", { "class": "iconUrl", "src": content.iconUrl }))
                                .append($("<div>", { "class": "title", "text": content.title }))
                                .append($("<div>", { "class": "publishBy", "text": content.publishBy }))
                                .append($("<div>", { "class": "publishAt", "text": content.publishAt }))
                                .append($("<div>", { "class": "source", "text": content.source }))
                                .append($("<div>", { "class": "originalUrl", "text": content.originalUrl })));
                    });
                })
                .fail(function (response) { })
                .always(function (response) { })
        }
    },
    youtubeapi: {
        placeholder: "YouTubeで検索",
        sname: "youtubeapi",
        theme_color: "red",
        filters: [],
        ajax: function () {
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
                            .append($("<button>", { "class": "col item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
                                .click(function () {
                                    modal_title.children().remove();
                                    modal_title.text(content.title);

                                    modal_body.children().remove();
                                    modal_body
                                        .append($("<iframe>", { "class": "w-100", "src": content.source }));

                                    modal_footer.children().remove();
                                    modal_footer
                                        .append($("<a>", { "class": "btn", "text": "Movie Download", "href": getVideoId("v=", false, content.originalUrl) }))
                                        .append($("<a>", { "class": "btn", "text": "Audio Download", "href": getVideoId("v=", true, content.originalUrl) }))
                                        .append($("<a>", { "class": "btn ms-auto", "text": "Original", "href": content.originalUrl }));
                                })
                                .append($("<img>", { "class": "iconUrl", "src": content.iconUrl }))
                                .append($("<div>", { "class": "title", "text": content.title }))
                                .append($("<div>", { "class": "publishBy", "text": content.publishBy }))
                                .append($("<div>", { "class": "publishAt", "text": content.publishAt }))
                                .append($("<div>", { "class": "source", "text": content.source }))
                                .append($("<div>", { "class": "originalUrl", "text": content.originalUrl })));
                    });
                })
                .fail(function (response) { })
                .always(function (response) { })
        }
    }
}

function getVideoId(param, toMp3, url) {
    const indexOfFirst = url.indexOf(param) + param.length;
    const videoId = url.substring(indexOfFirst, url.length);

    return "/youtube-dl?url="+ url +"&videoId=" + videoId + "&toMp3=" + toMp3;
}

function filter_setting(e) {
    const modal_body = $("#filterForm .modal-body");

    modal_body.children().remove();

    data[e].filters.forEach(filter => {
        const filter_setting = $("<div>", { "class": "filter-setting row mb-3" })
        const label = $("<div>", { "class": "col-3" }).append($("<div>", { "class": "filter-setting-label", "text": filter.label }));
        const value = $("<div>", { "class": "col-9" });

        filter.tag.forEach(t => value.append(t))

        filter_setting
            .append(label)
            .append(value);

        modal_body.append(filter_setting);
    });
    $("#search-form-filter-apply").click();
}

function calc_main_height() {
    const mainHeight = $("body").innerHeight() - $("header").outerHeight() - rem(1.5);
    $("main").innerHeight(mainHeight);
}

function rem(rem) {
    const fontSize = getComputedStyle(document.documentElement).fontSize;
    return rem * parseFloat(fontSize);
}

$(window).ready(function () {
    const localStorage_theme = localStorage.getItem("theme");
    const theme_api_name = localStorage_theme ? localStorage_theme : data.newsapi.sname;
    const theme_class_name = data[theme_api_name].theme_color;
    const placeholder = localStorage_theme ? data[localStorage_theme].placeholder : data.newsapi.placeholder;

    if(!localStorage_theme){
        localStorage.setItem("theme", "newsapi");
    }

    filter_setting(theme_api_name);
    calc_main_height();
    $("#search-form-query").attr("placeholder", placeholder);
    $("body").addClass(theme_class_name);
});

$("#logo").click(function () {
    const localStorage_theme = localStorage.getItem("theme");

    const theme = $("#theme-list option[data-api-name=" + localStorage_theme + "]");
    const theme_api_name = data[localStorage_theme].sname;
    const theme_class_name = data[localStorage_theme].theme_color;

    const next_theme = theme.next();
    const next_theme_api_name = next_theme.data('api-name');

    const new_theme = next_theme_api_name ? next_theme : $('#theme-list option:first');
    const new_theme_api_name = new_theme.data('api-name');

    const new_theme_class_name = data[new_theme_api_name].theme_color;

    localStorage.setItem("theme", new_theme_api_name);
    new_theme.prop('selected', 'selected');
    $("body").toggleClass(theme_class_name).toggleClass(new_theme_class_name);
    $("#search-form-query").attr("placeholder", data[new_theme_api_name].placeholder);
    filter_setting(new_theme_api_name);
    calc_main_height();
});

$("#search-form-query")
    .focusin(function () {
        $("#search-form").addClass("search-form-focusin");
    })
    .focusout(function () {
        $("#search-form").removeClass("search-form-focusin");
    })
    .on("input", function () {
        if ($(this).val()) {
            $("#search-form-submit").attr("disabled", false);
            $("#search-form-reset").show();
        }
        else {
            $("#search-form-submit").attr("disabled", true);
            $("#search-form-reset").hide();
        }
    });

$("#search-form-reset").click(function () {
    $("#search-form-submit").attr("disabled", true);
    $("#search-form-reset").hide();
});

$("#search-form-filter-apply").click(function () {
    const search_form_filter_tags = $("#search-form-filter-tags");

    search_form_filter_tags.children().remove();

    $("#filterForm .filter-setting").each(function () {
        const tag_label = $(this).find(".filter-setting-label");
        const tag_value = $(this).find(".filter-setting-value");
        var value = -1;

        if (tag_value.is("input")) {
            value = tag_value.val();
        } else if (tag_value.is("select")) {
            value = tag_value.find("option:selected").text();
        }

        search_form_filter_tags
            .append($("<div>", { "class": "col-auto" })
                .append($("<button>", { "class": "btn btn-border", "data-bs-toggle": "modal", "data-bs-target": "#filterForm" })
                    .append($("<div>", { "text": tag_label.text() }))
                    .append($("<div>", { "text": ":" }))
                    .append($("<div>", { "text": value, "name": tag_value.data("param-name") }))
                ))
    });
})

$("#search-form").on("submit", function (e) {
    const localStorage_theme = localStorage.getItem("theme");

    e.preventDefault();  // デフォルトのイベント(ページの遷移やデータ送信など)を無効にする
    $("html").removeClass("top-screen");
    data[localStorage_theme].ajax();
    calc_main_height();
});

$("#page-item-next").click(function () {
    const page = parseInt($("#page-item-value").val());

    $("#page-item-value").val(page + 1);
    $("#search-form-submit").click();
});

$("#page-item-prev").click(function () {
    const page = parseInt($("#page-item-value").val());

    $("#page-item-value").val(page - 1);
    $("#search-form-submit").click();
});

var oldScrollY = -1;
$("main").scroll(function () {

    const passed_point = oldScrollY > $("header").outerHeight() / 4;
    const isScrollUp = ($(this).scrollTop() - oldScrollY) < 0;
    const isScrollDown = (oldScrollY - $(this).scrollTop()) < 0;

    oldScrollY = $(this).scrollTop();

    if (passed_point && isScrollDown) {
        $("header").addClass("scrolling");
    }
    if (isScrollUp) {
        $("header").removeClass("scrolling");
    }
    calc_main_height();
});

$("#sign-list .sign").click(function () {
    $("#sign-list").hide();

    const that = $(this);

    const sign_detail = $("#sign-detail");

    sign_detail.find(".icons div").text(that.prop("title"));
    sign_detail.find(".icons img").prop("src", that.find("img").prop("src"));

    sign_detail.find(".stars.total div:last-child").attr("stars", that.data("stars-total"));
    sign_detail.find(".stars.love div:last-child").attr("stars", that.data("stars-love"));
    sign_detail.find(".stars.money div:last-child").attr("stars", that.data("stars-money"));
    sign_detail.find(".stars.job div:last-child").attr("stars", that.data("stars-job"));

    console.log(sign_detail.find(".stars.job div:last-child").data("stars"));

    sign_detail.find(".content").text(that.data("content"));

    sign_detail.find(".lucky-item").attr("lucky-item-value", that.data("lucky-item"));
    sign_detail.find(".lucky-color").attr("lucky-color-value", that.data("lucky-color"));

    sign_detail.show();
});

$("#sign-detail").click(function () {
    $("#sign-detail").hide();

    const sign_list = $("#sign-list");

    sign_list.show();
});