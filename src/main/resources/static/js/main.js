const modal_title = $("#item-window .modal-title");
const modal_body = $("#item-window .modal-body");
const modal_footer = $("#item-window .modal-footer");

// a : <数字>
// b : a | <記号>

// data -> <api> -> 
const data = {
    newsapi: {
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
                }
            })
                .done(function (response) {
                    $("#contents").children().remove();
                    response.contents.forEach(content => {
                        $("#contents")
                            .append($("<button>", { "class": "item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
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
                            .append($("<button>", { "class": "item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
                                .click(function () {
                                    modal_title.children().remove();
                                    modal_title.text(title);

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
    youtubeapi: {
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
                            .append($("<button>", { "class": "item btn", "data-bs-toggle": "modal", "data-bs-target": "#item-window" })
                                .click(function () {
                                    const param = 'v=';
                                    const indexOfFirst = content.originalUrl.indexOf(param) + param.length;
                                    const videoId = content.originalUrl.substring(indexOfFirst, content.originalUrl.length);

                                    const url = "/youtube-dl?videoId=" + videoId + "&toMp3=" + true;

                                    modal_title.children().remove();
                                    modal_title.text(content.title);

                                    modal_body.children().remove();
                                    modal_body
                                        .append($("<iframe>", { "class": "w-100", "src": content.source }));

                                    modal_footer.children().remove();
                                    modal_footer
                                        .append($("<a>", { "class": "btn", "text": "Download", "href": url }))
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

$("#theme-list option").click(function () { data[$(this).data('api-name')].ajax(); });

$(window).ready(function () {
    filter_setting("newsapi");
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

$("#logo").click(function () {
    const body = $("body");

    const theme = $('#theme-list option:selected');
    const theme_class_name = theme.data('class-name');

    const next_theme = theme.next();
    const next_theme_class_name = next_theme.data('class-name');

    const new_theme = next_theme_class_name ? next_theme : $('#theme-list option:first');
    const new_theme_class_name = new_theme.data('class-name');

    new_theme.prop('selected', 'selected');
    body.toggleClass(theme_class_name).toggleClass(new_theme_class_name);

    const theme_list_selected = $('#theme-list option:selected');

    $("#search-form-query").attr("placeholder", theme_list_selected.text() + "で検索");

    filter_setting(new_theme.data("api-name"));
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

//絞り込みの設定
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

$("#search-form").on("submit", function (e) {
    e.preventDefault();  // デフォルトのイベント(ページの遷移やデータ送信など)を無効にする
    $("html").removeClass("top-screen");
    $('#theme-list option:selected').click();
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

const header = $('header');
const headerHeight = header.height();

var flag = false;
var oldScrollY = -1;
// scrollイベントを設定
$(window).scroll(function () {

    const passed_point = oldScrollY > headerHeight / 4;

    const isScrollUp = ($(this).scrollTop() - oldScrollY) < 0;
    const isScrollDown = (oldScrollY - $(this).scrollTop()) < 0;

    oldScrollY = $(this).scrollTop();

    console.log("isScrollUp=" + isScrollUp + ", isScrollDown=" + isScrollDown);
    console.log("passed_point=" + passed_point);

    if(passed_point){
        if(isScrollDown){
            header.addClass("scrolling");
            flag = true;
        }
    }else{
        if(isScrollUp){
            header.removeClass("scrolling");
            flag = false;
        }
        flag = true;
    }

    console.log("flag=" + flag);
});

