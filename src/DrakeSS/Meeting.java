package DrakeSS;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.util.*;

/**
 * Created by Daylon on 7/1/2017.
 */
public class Meeting {

    int id;
    User creator;
    Calendar start;
    Calendar end;
    int room;
     Map<Integer,User> pendingUsers;
     Map<Integer,User> attendingUsers;
     Map<Integer, Proposal> proposals;

    public Meeting(int id, Calendar start, Calendar end, int room, User creator, Map<Integer, User> pendingUsers) throws MessagingException
    {
        this.id = id;
        this.start = start;
        this.end = end;
        this.room = room;
        this.creator = creator;
        this.pendingUsers = pendingUsers;
        this.attendingUsers = new HashMap<Integer, User>();
        this.proposals = new HashMap<Integer, Proposal>();

            for (int i = 0; i < pendingUsers.size(); i++)
            {
                EmailSender.sendEmail(pendingUsers.get(i).getEmail(), getInviteTitle(), getInviteBody(creator.getEmail()));
            }
    }

    public Meeting(int id, JSONObject meetingData)
    {
        this.id = id;

        this.start = Calendar.getInstance();
        start.set(Calendar.YEAR, meetingData.getJSONObject("startDate").getInt("Year"));
        start.set(Calendar.MONTH, meetingData.getJSONObject("startDate").getInt("Month"));
        start.set(Calendar.DAY_OF_MONTH, meetingData.getJSONObject("startDate").getInt("Day"));
        start.set(Calendar.HOUR_OF_DAY, meetingData.getJSONObject("startDate").getInt("Hour"));
        start.set(Calendar.MINUTE, meetingData.getJSONObject("startDate").getInt("Minute"));

        this.end = Calendar.getInstance();
        end.set(Calendar.YEAR, meetingData.getJSONObject("endDate").getInt("Year"));
        end.set(Calendar.MONTH, meetingData.getJSONObject("endDate").getInt("Month"));
        end.set(Calendar.DAY_OF_MONTH, meetingData.getJSONObject("endDate").getInt("Day"));
        end.set(Calendar.HOUR_OF_DAY, meetingData.getJSONObject("endDate").getInt("Hour"));
        end.set(Calendar.MINUTE, meetingData.getJSONObject("endDate").getInt("Minute"));

        this.room = meetingData.getInt("Room");
        this.creator = Client.userFromEmail(meetingData.getString("Creator"));

        JSONArray p = meetingData.getJSONArray("pendingUsers");
        pendingUsers = new HashMap<Integer, User>();
        for (int i = 0; i < p.length(); i++)
        {
            this.pendingUsers.put(this.pendingUsers.size(), Client.userFromEmail(p.getString(i)));
        }

        JSONArray a = meetingData.getJSONArray("attendingUsers");
        attendingUsers = new HashMap<Integer, User>();
        for (int i = 0; i < a.length(); i++)
        {
            this.attendingUsers.put(this.attendingUsers.size(), Client.userFromEmail(a.getString(i)));
        }

        JSONArray b = meetingData.getJSONArray("proposals");
        proposals = new HashMap<Integer, Proposal>();
        for (int i = 0; i < b.length(); i++)
        {
            JSONObject data = b.getJSONObject(i);
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, data.getInt("startHour"));
            start.set(Calendar.MINUTE, data.getInt("startMinutes"));
            end.set(Calendar.HOUR_OF_DAY, data.getInt("endHour"));
            end.set(Calendar.MINUTE, data.getInt("endMinutes"));
            this.proposals.put(this.proposals.size(), new Proposal(start, end, Client.userFromEmail(data.getString("user"))));
        }

    }

    public Map<Integer, Proposal> getProposals() {
        return proposals;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public Calendar getStart()
    {
        return start;
    }

    public Calendar getEnd()
    {
        return end;
    }

    public int getRoom()
    {
        return room;
    }

    public String getCreatorEmail()
    {
        return creator.getEmail();
    }
    public int getStartHour()
    {
        return start.get(Calendar.HOUR_OF_DAY);
    }

    public int getDisplayStartHour()
    {
        if (start.get(Calendar.HOUR) == 0)
        {
            return 12;
        }
        return start.get(Calendar.HOUR);
    }

    public String getStartMinutes()
    {
        if (start.get(Calendar.MINUTE) == 0)
        {
            return "00";
        }
        return Integer.toString(start.get(Calendar.MINUTE));
    }

    public String getStartDay()
    {
        return Integer.toString(start.get(Calendar.DAY_OF_MONTH));
    }

    public String getStartMonth()
    {
        return Integer.toString(start.get(Calendar.MONTH));
    }

    public String getStartYear()
    {
        return Integer.toString(start.get(Calendar.YEAR));
    }

    public int getEndHour()
    {
        if (end.get(Calendar.HOUR_OF_DAY) == 0)
        {
            return 12;
        }
        return end.get(Calendar.HOUR_OF_DAY);
    }

    public int getDisplayEndHour()
    {
        if (end.get(Calendar.HOUR) == 0)
        {
            return 12;
        }
        return end.get(Calendar.HOUR);
    }

    public String getEndMinutes()
    {
        if (end.get(Calendar.MINUTE) == 0)
        {
            return "00";
        }
        return Integer.toString(end.get(Calendar.MINUTE));
    }


    private String startToJSON() {
        String s = "{"
                + "\"Year\" : " + start.get(Calendar.YEAR) + ", "
                + "\"Month\" : " + start.get(Calendar.MONTH) + ", "
                + "\"Day\" : " + start.get(Calendar.DAY_OF_MONTH) + ","
                + "\"Hour\" : " + start.get(Calendar.HOUR_OF_DAY) + ","
                + "\"Minute\" : " + start.get(Calendar.MINUTE) + ","
                + "}";

        return s;
    }

    private String endToJSON() {
        String s = "{"
                + "\"Year\" : " + end.get(Calendar.YEAR) + ", "
                + "\"Month\" : " + end.get(Calendar.MONTH) + ", "
                + "\"Day\" : " + end.get(Calendar.DAY_OF_MONTH) + ","
                + "\"Hour\" : " + end.get(Calendar.HOUR_OF_DAY) + ","
                + "\"Minute\" : " + end.get(Calendar.MINUTE) + ","
                + "}";

        return s;
    }

    private JSONArray pendingToJSON() {
        JSONArray result = new JSONArray();
        for (int i = 0; i < pendingUsers.size(); i++)
        {
            if (pendingUsers.get(i) != null)
            {
                result.put(pendingUsers.get(i).getEmail());
            }
        }

        return result;
    }

    private JSONArray attendingToJSON() {
        JSONArray result = new JSONArray();
        for (int i = 0; i < attendingUsers.size(); i++)
        {
            if (attendingUsers.get(i) != null)
            {
                result.put(attendingUsers.get(i).getEmail());
            }
        }

        return result;
    }

    private JSONArray proposalsToJSON() {
        JSONArray result = new JSONArray();
        for (int i = 0; i < proposals.size(); i++)
        {
                JSONObject p = new JSONObject();
                p.put("startHour", proposals.get(i).getStart().get(Calendar.HOUR_OF_DAY));
                p.put("startMinutes", proposals.get(i).getStart().get(Calendar.MINUTE));
                p.put("endHour", proposals.get(i).getEnd().get(Calendar.HOUR_OF_DAY));
                p.put("endMinutes", proposals.get(i).getEnd().get(Calendar.MINUTE));
                p.put("user", proposals.get(i).getUserEmail());
                result.put(p);
        }

        return result;
    }


    public String toJSON() {
        String s = "{\"startDate\" : " + startToJSON() + ", "
                  + "\"endDate\" : " + endToJSON() + ", "
                  + "\"Room\" : " + room + ", "
                  + "\"Creator\" : " + creator.getEmail() + ", "
                  + "\"pendingUsers\" : " + pendingToJSON() + ", "
                  + "\"attendingUsers\" : " + attendingToJSON() + ", "
                  + "\"proposals\" : " + proposalsToJSON() + ", " + "}";
        return s;
    }

    public Map<Integer, User> getPendingUsers()
    {
        return pendingUsers;
    }

    public Map<Integer, User> getAttendingUsers()
    {
        return attendingUsers;
    }

    private String getInviteTitle() {
        String s = "New DrakeSS Meeting Invitation";
        return s;
    }

    private String getInviteBody(String email) {
        String s = email + " has invited you to a meeting through DrakeSS!"
                + "\n\nTo accept this invitation, please log in through the DrakeSS client "
                + "located on the computer next to the meeting room in the office."
                + "\n\nMeeting Details:"
                + "\nDate: " + start.get(Calendar.DAY_OF_MONTH) + "-" + start.get(Calendar.MONTH) + "-" + start.get(Calendar.YEAR)
                + "\nStart Time: " + getStartHour() + ":" + getStartMinutes()
                + "\nEnd Time: " + getEndHour() + ":" + getEndMinutes()
                + "\nLocation:  Room C" + getRoom()
                + "\n\nHow To Accept:"
                + "\nLogin with your email and password and navigate to your user account by clicking on your email address in the top right hand corner of the screen."
                + " There, you will see the user control panel options which allow you to accept an invitation."
                + "\n\nThank you for using DrakeSS. Have a great day!";

        return s;
    }

    private String getRescheduleTitle() {
        String s = "DrakeSS Meeting Reschedule";
        return s;
    }

    private String getRescheduleBody(String email) {
        String s = email + " has rescheduled a meeting you are scheduled to attend!"
                + "\n\nTo view your personal schedule, please log in through DrakeSS "
                + "located on the computer next to the meeting room in the office."
                + "\n\nNew Meeting Details:"
                + "\nDate: " + start.get(Calendar.DAY_OF_MONTH) + "-" + start.get(Calendar.MONTH) + "-" + start.get(Calendar.YEAR)
                + "\nStart Time: " + getStartHour() + ":" + getStartMinutes()
                + "\nEnd Time: " + getEndHour() + ":" + getEndMinutes()
                + "\nLocation:  Room C" + getRoom()
                + "\n\nWe apologize if this might conflict with your schedule. Please contact " + email + "to voice any additional concerns about this meeting.";

        return s;
    }

    public void sendReschedule() throws MessagingException
    {
        for (int i = 0; i < attendingUsers.size(); i++) {
            EmailSender.sendEmail(attendingUsers.get(i).getEmail(), getRescheduleTitle(), getRescheduleBody(creator.getEmail()));
        }
    }

}
