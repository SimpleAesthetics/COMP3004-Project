package reboot.grouper.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
	@SerializedName("nickname")
	@Expose
	private String nickname;

	@SerializedName("questionAnswers")
	@Expose
	private List<Integer> questionAnswers;

	public User() {
		this.nickname = null;
		this.questionAnswers = new ArrayList<Integer>();
	}
	
	public User(String nickname) {
		this.nickname = nickname;
		this.questionAnswers = new ArrayList<>();
	}
	
	public User(String nickname, List<Integer> questionAnswers) {
		this.nickname = nickname;
		this.questionAnswers = questionAnswers;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<Integer> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List<Integer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((questionAnswers == null) ? 0 : questionAnswers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (questionAnswers == null) {
			if (other.questionAnswers != null)
				return false;
		} else if (!questionAnswers.equals(other.questionAnswers))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [nickname=" + nickname + ", questionAnswers=" + questionAnswers + "]";
	}
	
	
}
