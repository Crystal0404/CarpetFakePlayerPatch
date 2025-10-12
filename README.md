## Fabric Example Mod Kotlin

以kotlin作为构建脚本的Fabric模组模板, 额外安装了`me.modmuss50.mod-publish-plugin`插件用于将模组发布到各个平台,
插件文档[https://modmuss50.github.io/mod-publish-plugin](https://modmuss50.github.io/mod-publish-plugin/)

### 使用

- 全局搜索`[FUNCTION]`, 设置发布插件
- 设置完成后在github发布release即可自动向全平台推送, 更改日志即为release的内容.
- 也可以手动触发release action, 需填入release的tag. tag不存在action会自动失败

### `minecraft_version_range`填写示例

- 如果你只支持 `mc1.21.5`, 你可以填写`1.21.5`
- 如果你支持 `mc1.21-mc1.21.5`之间全部版本, 你可以填写 `1.21-1.21.5`
- 如果你希望使用其它分割符号而不是 `-`, 你可以修改 `split`

*注意: 如果你需要发布快照版本, 不要使用 `-` 作为分割符*
