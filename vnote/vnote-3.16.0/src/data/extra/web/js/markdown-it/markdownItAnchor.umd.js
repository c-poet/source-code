!function(n,e){"object"==typeof exports&&"undefined"!=typeof module?module.exports=e():"function"==typeof define&&define.amd?define(e):(n=n||self).markdownItAnchor=e()}(this,function(){var n={false:"push",true:"unshift"},e=Object.prototype.hasOwnProperty,t=function(n,t,r,i){var u=n,o=i;if(r&&e.call(t,u))throw Error("User defined id attribute '"+n+"' is NOT unique. Please fix it in your markdown to continue.");for(;e.call(t,u);)u=n+"-"+o++;return t[u]=!0,u},r=function n(e,r){r=Object.assign({},n.defaults,r),e.core.ruler.push("anchor",function(n){var e,i={},u=n.tokens,o=Array.isArray(r.level)?(e=r.level,function(n){return e.includes(n)}):function(n){return function(e){return e>=n}}(r.level);u.filter(function(n){return"heading_open"===n.type}).filter(function(n){return o(Number(n.tag.substr(1)))}).forEach(function(e){var o=u[u.indexOf(e)+1].children.filter(function(n){return"text"===n.type||"code_inline"===n.type}).reduce(function(n,e){return n+e.content},""),c=e.attrGet("id");c=null==c?t(r.slugify(o),i,!1,r.uniqueSlugStartIndex):t(c,i,!0,r.uniqueSlugStartIndex),e.attrSet("id",c),r.permalink&&r.renderPermalink(c,r,n,u.indexOf(e)),r.callback&&r.callback(e,{slug:c,title:o})})})};return r.defaults={level:1,slugify:function(n){return encodeURIComponent(String(n).trim().toLowerCase().replace(/\s+/g,"-"))},uniqueSlugStartIndex:1,permalink:!1,renderPermalink:function(e,t,r,i){var u,o=[Object.assign(new r.Token("link_open","a",1),{attrs:[].concat(t.permalinkClass?[["class",t.permalinkClass]]:[],[["href",t.permalinkHref(e,r)]],Object.entries(t.permalinkAttrs(e,r)))}),Object.assign(new r.Token("html_block","",0),{content:t.permalinkSymbol}),new r.Token("link_close","a",-1)];t.permalinkSpace&&o[n[!t.permalinkBefore]](Object.assign(new r.Token("text","",0),{content:" "})),(u=r.tokens[i+1].children)[n[t.permalinkBefore]].apply(u,o)},permalinkClass:"header-anchor",permalinkSpace:!0,permalinkSymbol:"¶",permalinkBefore:!1,permalinkHref:function(n){return"#"+n},permalinkAttrs:function(n){return{}}},r});
//# sourceMappingURL=markdownItAnchor.umd.js.map
