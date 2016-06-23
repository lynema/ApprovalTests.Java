package org.approvaltests.combinations;

import java.util.ArrayList;
import java.util.List;

import org.approvaltests.Approvals;
import org.approvaltests.legacycode.LegacyApprovals;
import org.lambda.functions.Function1;
import org.lambda.functions.Function2;
import org.lambda.functions.Function9;

public class CombinationApprovals
{
  private static final Object EMPTY_ENTRY = new Object();
  private static final Object EMPTY[]     = {EMPTY_ENTRY};
  public static void verifyAllCombinations(Object call, String method, Object[]... parametersVariations)
      throws Exception
  {
    LegacyApprovals.LockDown(call, method, parametersVariations);
  }
  /**
   * Use SkipCombination exception for invalid combinations
   */
  public static <IN1, OUT> void verifyAllCombinations(Function1<IN1, OUT> call, IN1[] parameters1) throws Exception
  {
    verifyAllCombinations((n1, n2) -> call.call(n1), parameters1, EMPTY);
  }
  /**
   * Use SkipCombination exception for invalid combinations
   */
  public static <IN1, IN2, OUT> void verifyAllCombinations(Function2<IN1, IN2, OUT> call, IN1[] parameters1,
      IN2[] parameters2) throws Exception
  {
    StringBuffer output = new StringBuffer();
    for (IN1 in1 : parameters1)
    {
      for (IN2 in2 : parameters2)
      {
        String result;
        try
        {
          result = "" + call.call(in1, in2);
        }
        catch (SkipCombination e)
        {
          continue;
        }
        catch (Throwable e)
        {
          result = e.getMessage();
        }
        output.append(String.format("%s => %s \r\n", extracted(in1, in2), result));
      }
    }
    Approvals.verify(output);
  }
  /**
   * Use SkipCombination exception for invalid combinations
   */
  public static <IN1, IN2, IN3, IN4, IN5, IN6, IN7, IN8, IN9, OUT> void verifyAllCombinations(
      Function9<IN1, IN2, IN3, IN4, IN5, IN6, IN7, IN8, IN9, OUT> call, IN1[] parameters1, IN2[] parameters2,
      IN3[] parameters3, IN4[] parameters4, IN5[] parameters5, IN6[] parameters6, IN7[] parameters7,
      IN8[] parameters8, IN9[] parameters9) throws Exception
  {
    StringBuffer output = new StringBuffer();
    for (IN1 in1 : parameters1)
    {
      for (IN2 in2 : parameters2)
      {
        for (IN3 in3 : parameters3)
        {
          for (IN4 in4 : parameters4)
          {
            for (IN5 in5 : parameters5)
            {
              for (IN6 in6 : parameters6)
              {
                for (IN7 in7 : parameters7)
                {
                  for (IN8 in8 : parameters8)
                  {
                    for (IN9 in9 : parameters9)
                    {
                      String result;
                      try
                      {
                        result = "" + call.call(in1, in2, in3, in4, in5, in6, in7, in8, in9);
                      }
                      catch (SkipCombination e)
                      {
                        continue;
                      }
                      catch (Throwable e)
                      {
                        result = e.getMessage();
                      }
                      output.append(String.format("%s => %s \r\n",
                          extracted(in1, in2, in3, in4, in5, in6, in7, in8, in9), result));
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    Approvals.verify(output);
  }
  private static List<Object> extracted(Object... objects)
  {
    List<Object> list = new ArrayList<>();
    for (Object object : objects)
    {
      if (object != EMPTY_ENTRY)
      {
        list.add(object);
      }
    }
    return list;
  }
}