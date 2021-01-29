#-*- coding = utf-8 -*-
#@File  : drawScript.py
#@Time  : 2021/1/16 16:49
#@Author: Chien
#@Software:PyCharm

from pyecharts import options as opts
from pyecharts.charts import Graph,Grid,Page,Line
from pyecharts.commons.utils import JsCode
from pyecharts.components import Table
from pyecharts.options import ComponentTitleOpts
import getopt
import json
import sys
import time


numTOP=20
tmpPath="/Users/zhangjian/IdeaProjects/ScholarQuery/tmp/"

def NetGraph(nodes,links,TYPE):
    if TYPE == "SocialNet":
        title='SocialNet'
        symbol='circle'
    if TYPE == "PublicationsNet":
        Title="PulicationsNet"
        symbol = 'diamond'
    sn = (
        Graph(init_opts=opts.InitOpts(chart_id=1,width="1350px",height="750px"))
            .add("", nodes, links, repulsion=4000,symbol=symbol)
            .set_global_opts(title_opts=opts.TitleOpts(title=title))
    )
    return sn

def citedLine(x_data,y_data):
    background_color_js = (
        "new echarts.graphic.LinearGradient(0, 0, 0, 1, "
        "[{offset: 0, color: '#c86589'}, {offset: 1, color: '#06a7ff'}], false)"
    )
    area_color_js = (
        "new echarts.graphic.LinearGradient(0, 0, 0, 1, "
        "[{offset: 0, color: '#eb64fb'}, {offset: 1, color: '#3fbbff0d'}], false)"
    )
    cl = (
        Line(init_opts=opts.InitOpts(bg_color=JsCode(background_color_js),width="1350px",height="750px"))
            .add_xaxis(xaxis_data=x_data)
            .add_yaxis(
            series_name="cites",
            y_axis=y_data,
            is_smooth=True,
            is_symbol_show=True,
            symbol="circle",
            symbol_size=6,
            linestyle_opts=opts.LineStyleOpts(color="#fff"),
            label_opts=opts.LabelOpts(is_show=True, position="top", color="white"),
            itemstyle_opts=opts.ItemStyleOpts(
                color="red", border_color="#fff", border_width=3
            ),
            tooltip_opts=opts.TooltipOpts(is_show=False),
            areastyle_opts=opts.AreaStyleOpts(color=JsCode(area_color_js), opacity=1),
        )
            .set_global_opts(
            title_opts=opts.TitleOpts(
                title="Cites per Years",
                pos_bottom="92%",
                pos_left="8%",
                title_textstyle_opts=opts.TextStyleOpts(color="#fff", font_size=16),
            ),
            xaxis_opts=opts.AxisOpts(
                type_="category",
                boundary_gap=False,
                axislabel_opts=opts.LabelOpts(margin=30, color="#ffffff63"),
                axisline_opts=opts.AxisLineOpts(is_show=False),
                axistick_opts=opts.AxisTickOpts(
                    is_show=True,
                    length=25,
                    linestyle_opts=opts.LineStyleOpts(color="#ffffff1f"),
                ),
                splitline_opts=opts.SplitLineOpts(
                    is_show=True, linestyle_opts=opts.LineStyleOpts(color="#ffffff1f")
                ),
            ),
            yaxis_opts=opts.AxisOpts(
                type_="value",
                position="right",
                axislabel_opts=opts.LabelOpts(margin=20, color="#ffffff63"),
                axisline_opts=opts.AxisLineOpts(
                    linestyle_opts=opts.LineStyleOpts(width=2, color="#fff")
                ),
                axistick_opts=opts.AxisTickOpts(
                    is_show=True,
                    length=15,
                    linestyle_opts=opts.LineStyleOpts(color="#ffffff1f"),
                ),
                splitline_opts=opts.SplitLineOpts(
                    is_show=True, linestyle_opts=opts.LineStyleOpts(color="#ffffff1f")
                ),
            ),
            legend_opts=opts.LegendOpts(is_show=False),
        )
    )
    return cl

if __name__ == "__main__":
    opt,args= getopt.getopt(sys.argv[1:],"",["type=", "scholar=","message="])
    for option, argument in opt:
        if option in ("--type"):
            Type = argument
        if option in ("--scholar"):
            scholar = argument
        if option in ("--message"):
            message = argument
    if Type == "SocialNet":
        coauthorsInfo = json.loads(message, strict=False)
        nodes_social = []
        links_social = []
        nodes_social.append(opts.GraphNode(name=scholar, symbol_size=50))
        for scho in coauthorsInfo:
            nodes_social.append(opts.GraphNode(name=scho["name"], symbol_size=30))
            links_social.append(opts.GraphLink(source=scholar, target=scho["name"]))
        tempFile=tmpPath + '{}_sc.html'.format(scholar.replace(" ","_"))
        NetGraph(nodes_social,links_social,Type).render(tempFile)
        print(tempFile)
    # elif TYPE == "publicationsNet":
    #     publicationsList = ' '.join(msgList)
    #     publicationsList = publicationsList.split('&$&')
    #     publicationsList = list(set(publicationsList))
    #     root = "PUBLICATIONS"
    #     nodes_social = []
    #     links_social = []
    #     nodes_social.append(opts.GraphNode(name=root, symbol_size=30))
    #     for item in publicationsList:
    #         nodes_social.append(opts.GraphNode(name=item, symbol_size=10))
    #         links_social.append(opts.GraphLink(source=root, target=item))
    #     tempFile = tmpPath + '{}_pb.html'.format(''.join(list(str(int(time.time()))))[-6:])
    #     NetGraph(nodes_social, links_social, TYPE).render(tempFile)
    #     print(tempFile)
    # elif TYPE == "citedLine":
    #     x_data=[]
    #     y_data=[]
    #     citeData = ''.join(msgList).replace("-",',').replace("\'","\"")
    #     citeData = json.loads(citeData)
    #     for item in citeData:
    #         x_data.append(item)
    #         y_data.append(citeData[item])
    #     tempFile = tmpPath + '{}_line.html'.\
    #         format(''.join(list(str(int(time.time()))))[-6:])
    #     citedLine(x_data,y_data).render(tempFile)
    #     print(tempFile)
    # else:
    #     print("ERROR".fomat(TYPE))