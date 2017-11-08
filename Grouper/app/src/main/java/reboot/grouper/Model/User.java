package reboot.grouper.Model;


import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User> {
	
	private String nickname;
	private List<Integer> questionAnswers;

	public User(String nickname) {
		this.nickname = nickname;
		this.questionAnswers = new ArrayList<>();
	}
	
	public User(String nickname, List<Integer> questionAnswers) {
		this.nickname = nickname;
		this.questionAnswers = questionAnswers;
	}

	@Override
	public int compareTo(User otherUser) {
		
		List<Integer> oneQuestionAns = this.getQuestionAnswers();
		List<Integer> twoQuestionAns = otherUser.getQuestionAnswers();
		
		if ((oneQuestionAns==null && twoQuestionAns==null) || 
	        (oneQuestionAns.size() != twoQuestionAns.size())) {
			
			return 0;
		}
		
		int score = 0; 
			
		System.out.println("User Comparison: " + this.getNickname() + " " + otherUser.getNickname());
		
		for (int i = 0; i < oneQuestionAns.size(); ++i) {
			
			if (oneQuestionAns.get(i).equals(twoQuestionAns.get(i))) {
				score++;
			} else {
				score--;
			}
		}
		
		System.out.println("score: "+score);
		
		return score;
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
