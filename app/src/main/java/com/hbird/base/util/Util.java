package com.hbird.base.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//https://www.jianshu.com/p/cd33fc5c55dc 微信官方demo
public class Util {

    private static final String TAG = "SDK_Sample.Util";

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // ���������ļ���С������
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	/*public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}*/

    public static Date stringToDate(String day, String pattern) {
        DateFormat format1 = new SimpleDateFormat(pattern);
        try {
            Date date = format1.parse(day);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getMonthList(String startDay, String endDay) {
        List<String> list = new ArrayList<>();
        String[] start = startDay.split("-");
        String[] end = endDay.split("-");

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(stringToDate(startDay, "yyyy-MM"));
        calendar1.add(Calendar.MONTH, -1);//增加一个月
        System.out.println("增加月份后的日期：" + calendar1.getTime());
        String nowDate1 = calendar1.get(Calendar.YEAR) + "-" + ((calendar1.get(Calendar.MONTH) + 1) < 10 ? ("0" + (calendar1.get(Calendar.MONTH) + 1)) : (calendar1.get(Calendar.MONTH) + 1));
        list.add(nowDate1);

        list.add(startDay);

        String nowDate = startDay;
        while (compareDate(nowDate, endDay, "yyyy-MM") == -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(stringToDate(list.get(list.size() - 1), "yyyy-MM"));
            calendar.add(Calendar.MONTH, 1);//增加一个月
            System.out.println("增加月份后的日期：" + calendar.getTime());
            nowDate = calendar.get(Calendar.YEAR) + "-" + ((calendar.get(Calendar.MONTH) + 1) < 10 ? ("0" + (calendar.get(Calendar.MONTH) + 1)) : (calendar.get(Calendar.MONTH) + 1));
            list.add(nowDate);
        }
        return list;
    }

    public static String add0(int s) {
        if (s < 10) {
            return "0" + s;
        }
        return s + "";
    }

    // 获取日数据
    public static List<String> getDayList(String startDay, String endDay) {
        List<String> list = new ArrayList<>();
        String[] start = startDay.split("-");
        String[] end = endDay.split("-");

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(stringToDate(startDay, "yyyy-MM-dd"));
        calendar1.add(Calendar.DATE, -1);//昨天
        String nowDate1 = calendar1.get(Calendar.YEAR) + "-" + add0(calendar1.get(Calendar.MONTH) + 1) + "-" + add0(calendar1.get(Calendar.DATE));
        list.add(nowDate1);

        list.add(startDay);

        String nowDate = startDay;
        while (compareDate(nowDate, endDay, "yyyy-MM-dd") == -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(stringToDate(list.get(list.size() - 1), "yyyy-MM-dd"));
            calendar.add(Calendar.DATE, 1);//增加一个月
            nowDate = calendar.get(Calendar.YEAR) + "-" + add0((calendar.get(Calendar.MONTH) + 1)) + "-" + add0(calendar.get(Calendar.DATE));
            list.add(nowDate);
        }
        return list;
    }

    // 获取周数据
    public static List<String> getWeekList(String startDay, String endDay) {
        List<String> list = new ArrayList<>();
        String[] start = startDay.split("-"); // 2018-48
        String[] end = endDay.split("-");// 2019-02

        if (start[0].equals(end[0])) {// 没有跨年
            int startNum = Integer.parseInt(start[1]);
            int endNum = Integer.parseInt(end[1]);
            list.add(start[0] + "-" + startNum);
            int now = startNum+1;
            while (now <= endNum) {
                list.add(start[0] +"-"+add0(now));
                now++;
            }
        } else {// 跨年了
            // 第一年的最大周
            int startMaxWeek = getMaxWeekNumOfYear(Integer.parseInt(start[0]));
            int startNum = Integer.parseInt(start[1]);
            int endNum = Integer.parseInt(end[1]);
            list.add(start[0] + "-" + startNum);
            int now = startNum+1;
            while (now <= startMaxWeek) {
                list.add(start[0] +"-"+add0(now));
                now++;
            }
            now = 1;
            while (now <= endNum) {
                list.add(end[0] +"-"+add0(now));
                now++;
            }
        }
        return list;
    }

    // 判断两个日期前后
    public static int compareDate(String DATE1, String DATE2, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    // 判断两个日期前后
    public static int compareDate(Date dt1, Date dt2) {
        try {
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    // 获取当前时间所在年的最大周数
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    // 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }
}
