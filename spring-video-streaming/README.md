# Spring video streaming over HTTP

## How To convert video to FFmpeg format

```
ffmpeg -i input.mov \
  -codec: copy \
  -start_number 0 \
  -hls_time 5 \
  -hls_list_size 0 \
  -f hls \
  videos/test/test.m3u8
```