## What is this?

ColorMyCodes allows you to edit existing and add additional color codes to minecraft.

**The default configuration matches color codes in bedrock edition.**

## How do I add my own?

After starting the mod once a config file should appear at `.minecraft/config/colormycodes/config.json`.

The file is structured like this:

```json
{
  "version": 1,
  "content": {
    "aqua": {
      "code": "a",
      "color": "#00AAAA"
    },
    "obfuscated": {
      "code": "k",
      "modifier": true
    },
    "underline": {}
  }
}
```

`version` is always `1`.

`content` contains a map of `names` to their configuration.\
If `content` equals `{}` the value is removed from vanilla.

`code` is the letter after `§` which you need to use the formatting. (e.g. `"code": "i"` -> `§i`)

`color` is the color which the text changes to.

`modifier` determines if color changes are keep.